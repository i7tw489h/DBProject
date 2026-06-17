package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.canteen.entity.*;
import com.campus.canteen.mapper.*;
import com.campus.canteen.service.AIService;
import com.campus.canteen.service.AIRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIRecommendServiceImpl implements AIRecommendService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRestrictionsMapper userRestrictionsMapper;

    @Autowired
    private AIService aiService;

    @Override
    public List<Dish> recommendByHistory(Long userId, int limit) {
        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("user_id", userId);
        List<Order> orders = orderMapper.selectList(orderQuery);

        Map<Long, Integer> dishCountMap = new HashMap<>();
        for (Order order : orders) {
            QueryWrapper<OrderItem> itemQuery = new QueryWrapper<>();
            itemQuery.eq("order_id", order.getOrderId());
            List<OrderItem> items = orderItemMapper.selectList(itemQuery);
            for (OrderItem item : items) {
                dishCountMap.merge(item.getDishId(), item.getQuantity(), Integer::sum);
            }
        }

        List<Long> sortedDishIds = dishCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit * 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Dish> allDishes = dishMapper.selectAllWithNutrition();
        List<Dish> recommendedDishes = new ArrayList<>();
        // 视图v_dish_nutrition已经过滤了上架菜品
        for (Long dishId : sortedDishIds) {
            for (Dish dish : allDishes) {
                if (dish.getDishId().equals(dishId)) {
                    recommendedDishes.add(dish);
                    break;
                }
            }
        }

        if (recommendedDishes.size() < limit) {
            List<Dish> popularDishes = allDishes.stream()
                    .filter(d -> d.getSalesCount() != null)
                    .sorted((d1, d2) -> d2.getSalesCount().compareTo(d1.getSalesCount()))
                    .limit(limit)
                    .collect(Collectors.toList());

            for (Dish dish : popularDishes) {
                if (!recommendedDishes.contains(dish) && recommendedDishes.size() < limit) {
                    recommendedDishes.add(dish);
                }
            }
        }

        return recommendedDishes.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<Dish> recommendByGoal(Long userId, int dietGoal, int limit) {
        List<Dish> allDishes = dishMapper.selectAllWithNutrition();
        // 视图v_dish_nutrition已经过滤了上架菜品
        List<Dish> filteredDishes = new ArrayList<>(allDishes);

        // 随机打乱所有菜品，增加多样性
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        java.util.Collections.shuffle(filteredDishes, random);

        List<Dish> recommendedDishes = new ArrayList<>();

        switch (dietGoal) {
            case 1: // 减脂：优先推荐低热量菜品（≤400kcal），如果不够再放宽
                List<Dish> lowCalorieDishes = filteredDishes.stream()
                        .filter(d -> d.getCalories() != null && d.getCalories().compareTo(new BigDecimal("400")) <= 0)
                        .sorted((d1, d2) -> d1.getCalories().compareTo(d2.getCalories()))
                        .limit(limit)
                        .collect(Collectors.toList());
                
                if (lowCalorieDishes.size() < limit) {
                    // 不足时，从所有菜品中再补充
                    List<Dish> supplement = filteredDishes.stream()
                            .filter(d -> d.getCalories() == null || d.getCalories().compareTo(new BigDecimal("600")) <= 0)
                            .filter(d -> lowCalorieDishes.stream().noneMatch(x -> x.getDishId().equals(d.getDishId())))
                            .limit(limit - lowCalorieDishes.size())
                            .collect(Collectors.toList());
                    lowCalorieDishes.addAll(supplement);
                }
                recommendedDishes = lowCalorieDishes;
                break;
            case 2: // 增肌：优先推荐高蛋白菜品（≥10g），如果不够再放宽
                List<Dish> highProteinDishes = filteredDishes.stream()
                        .filter(d -> d.getProtein() != null && d.getProtein().compareTo(new BigDecimal("10")) >= 0)
                        .sorted((d1, d2) -> d2.getProtein().compareTo(d1.getProtein()))
                        .limit(limit)
                        .collect(Collectors.toList());
                
                if (highProteinDishes.size() < limit) {
                    List<Dish> supplement = filteredDishes.stream()
                            .filter(d -> d.getProtein() != null && d.getProtein().compareTo(new BigDecimal("5")) >= 0)
                            .filter(d -> highProteinDishes.stream().noneMatch(x -> x.getDishId().equals(d.getDishId())))
                            .limit(limit - highProteinDishes.size())
                            .collect(Collectors.toList());
                    highProteinDishes.addAll(supplement);
                }
                recommendedDishes = highProteinDishes;
                break;
            case 3: // 养胃：推荐不辣、低脂的菜品
                recommendedDishes = filteredDishes.stream()
                        .filter(d -> {
                            if (d.getName() == null) return true;
                            String name = d.getName().toLowerCase();
                            return !name.contains("辣") && !name.contains("麻") && !name.contains("椒");
                        })
                        .filter(d -> d.getFat() == null || d.getFat().compareTo(new BigDecimal("20")) <= 0)
                        .limit(limit)
                        .collect(Collectors.toList());
                break;
            default: // 保持健康：随机推荐
                recommendedDishes = filteredDishes.stream()
                        .limit(limit)
                        .collect(Collectors.toList());
        }

        // 兜底：如果没有符合条件的菜品，从所有菜品中随机取
        if (recommendedDishes.isEmpty()) {
            recommendedDishes = filteredDishes.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return recommendedDishes;
    }

    @Override
    public List<Dish> recommendByRestrictions(Long userId, int limit) {
        LambdaQueryWrapper<UserRestrictions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRestrictions::getUserId, userId);
        List<UserRestrictions> restrictions = userRestrictionsMapper.selectList(wrapper);

        List<Dish> allDishes = dishMapper.selectAllWithNutrition();
        // 视图v_dish_nutrition已经过滤了上架菜品
        List<Dish> filteredDishes = new ArrayList<>(allDishes);

        for (UserRestrictions restriction : restrictions) {
            String restrictionValue = restriction.getRestrictionValue();
            if (restrictionValue != null) {
                filteredDishes = filteredDishes.stream()
                        .filter(d -> {
                            String name = d.getName() != null ? d.getName().toLowerCase() : "";
                            String ingredients = d.getIngredients() != null ? d.getIngredients().toLowerCase() : "";
                            return !name.contains(restrictionValue.toLowerCase()) && !ingredients.contains(restrictionValue.toLowerCase());
                        })
                        .collect(Collectors.toList());
            }
        }

        // 兜底：如果没有符合条件的菜品，返回所有菜品中的热门
        if (filteredDishes.isEmpty()) {
            filteredDishes = new ArrayList<>(allDishes);
        }

        return filteredDishes.stream()
                .sorted((d1, d2) -> {
                    Integer s1 = d1.getSalesCount();
                    Integer s2 = d2.getSalesCount();
                    if (s1 == null && s2 == null) return 0;
                    if (s1 == null) return 1;
                    if (s2 == null) return -1;
                    return s2.compareTo(s1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCombinedRecommendations(Long userId) {
        System.out.println("=== getCombinedRecommendations 开始 ===");
        System.out.println("用户ID: " + userId);
        
        List<Map<String, Object>> result = new ArrayList<>();

        User user = userMapper.selectById(userId);
        System.out.println("查询到的用户: " + (user != null ? "有" : "无"));
        int dietGoal = user != null && user.getDietGoal() != null ? user.getDietGoal() : 0;

        List<Dish> historyRecommendations = recommendByHistory(userId, 4);
        Map<String, Object> historyResult = new HashMap<>();
        historyResult.put("type", "history");
        historyResult.put("title", "猜你喜欢");
        historyResult.put("description", "根据您的历史点餐记录推荐");
        historyResult.put("dishes", historyRecommendations);
        result.add(historyResult);

        List<Dish> goalRecommendations = recommendByGoal(userId, dietGoal, 4);
        Map<String, Object> goalResult = new HashMap<>();
        goalResult.put("type", "goal");
        String goalTitle = "目标推荐";
        switch (dietGoal) {
            case 1: goalTitle = "减脂推荐"; break;
            case 2: goalTitle = "增肌推荐"; break;
            case 3: goalTitle = "养胃推荐"; break;
        }
        goalResult.put("title", goalTitle);
        goalResult.put("description", "根据您的饮食目标推荐");
        goalResult.put("dishes", goalRecommendations);
        result.add(goalResult);

        List<Dish> restrictionRecommendations = recommendByRestrictions(userId, 4);
        Map<String, Object> restrictionResult = new HashMap<>();
        restrictionResult.put("type", "restriction");
        restrictionResult.put("title", "符合忌口");
        restrictionResult.put("description", "已过滤您的忌口菜品");
        restrictionResult.put("dishes", restrictionRecommendations);
        result.add(restrictionResult);

        System.out.println("=== getCombinedRecommendations 完成 ===");
        System.out.println("推荐结果数量: " + result.size());

        // 随机打乱每个推荐组中的菜品顺序，实现"换一批"效果
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        System.out.println("随机种子: " + seed);

        for (Map<String, Object> group : result) {
            List<Dish> dishes = (List<Dish>) group.get("dishes");
            if (dishes != null && dishes.size() > 1) {
                System.out.println("打乱前 - " + group.get("title") + ": " + dishes.stream().map(Dish::getName).collect(Collectors.toList()));
                Collections.shuffle(dishes, random);
                System.out.println("打乱后 - " + group.get("title") + ": " + dishes.stream().map(Dish::getName).collect(Collectors.toList()));
            }
        }

        // 为每个菜品独立调用存储函数获取标签
        fillGroupLabels(result);

        return result;
    }

    /**
     * 为每个菜品独立计算标签和推荐度
     * 猜你喜欢组：推荐度评分（数字）
     * 目标推荐/符合忌口组：健康评级（优秀/良好/推荐值）
     */
    private void fillGroupLabels(List<Map<String, Object>> result) {
        for (Map<String, Object> group : result) {
            String type = (String) group.get("type");
            @SuppressWarnings("unchecked")
            List<Dish> dishes = (List<Dish>) group.get("dishes");
            if (dishes == null) continue;

            for (Dish dish : dishes) {
                try {
                    if ("history".equals(type)) {
                        // 猜你喜欢组：推荐度取自菜品价格（保留2位小数）
                        java.math.BigDecimal price = dish.getPrice();
                        if (price == null) {
                            price = java.math.BigDecimal.ZERO;
                        }
                        // 格式化为保留2位小数
                        java.math.BigDecimal recommendValue = price.setScale(2, java.math.RoundingMode.HALF_UP);
                        dish.setRecommendScore(recommendValue);
                        dish.setHealthRating("推荐度: " + recommendValue);
                    } else {
                        // 目标推荐/符合忌口组：调用存储函数计算健康评级
                        String rating = dishMapper.calcHealthRating(
                                dish.getCalories(),
                                dish.getProtein(),
                                dish.getFat(),
                                dish.getSodium());
                        dish.setHealthRating(rating);
                    }
                } catch (Exception e) {
                    System.err.println("获取菜品 " + dish.getName() + " 标签失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 调用数据库存储函数 fn_calc_health_rating 计算每个菜品的健康评级
     */
    private void fillHealthRating(List<Dish> dishes) {
        if (dishes == null || dishes.isEmpty()) {
            return;
        }
        for (Dish dish : dishes) {
            try {
                String rating = dishMapper.calcHealthRating(
                        dish.getCalories(),
                        dish.getProtein(),
                        dish.getFat(),
                        dish.getSodium());
                dish.setHealthRating(rating);
            } catch (Exception e) {
                System.err.println("计算菜品 " + dish.getName() + " 健康评级失败: " + e.getMessage());
                dish.setHealthRating("推荐值");
            }
        }
    }

    @Override
    public List<Dish> intelligentMeal(Long userId, String mealType) {
        System.out.println("=== 智能配餐开始 ===");
        System.out.println("用户ID: " + userId + ", 餐类型: " + mealType);
        
        List<Dish> allDishes = dishMapper.selectAllWithNutrition();
        System.out.println("数据库菜品总数: " + allDishes.size());
        
        // 视图v_dish_nutrition已经过滤了上架菜品，所以不需要再检查is_shelf
        List<Dish> availableDishes = new ArrayList<>(allDishes);
        System.out.println("上架菜品数量: " + availableDishes.size());

        List<Dish> spicyDishes = getSpicyDishes(availableDishes);
        System.out.println("辛辣菜品数量: " + spicyDishes.size());
        availableDishes.removeAll(spicyDishes);
        System.out.println("可用菜品数量(去辣后): " + availableDishes.size());

        // 动态构建菜品营养数据字符串（包含所有营养成分）
        StringBuilder dishNutritionData = new StringBuilder();
        for (Dish dish : availableDishes) {
            String name = dish.getName() != null ? dish.getName() : "未知菜品";
            Double calories = dish.getCalories() != null ? dish.getCalories().doubleValue() : 0;
            Double protein = dish.getProtein() != null ? dish.getProtein().doubleValue() : 0;
            Double fat = dish.getFat() != null ? dish.getFat().doubleValue() : 0;
            Double carbs = dish.getCarbs() != null ? dish.getCarbs().doubleValue() : 0;
            Double sodium = dish.getSodium() != null ? dish.getSodium().doubleValue() : 0;
            Double fiber = dish.getFiber() != null ? dish.getFiber().doubleValue() : 0;
            dishNutritionData.append(String.format("%s：热量%.0fkcal，蛋白质%.0fg，脂肪%.0fg，碳水%.0fg，钠%.0fmg，膳食纤维%.0fg\n", 
                name, calories, protein, fat, carbs, sodium, fiber));
        }
        System.out.println("可用菜品及营养数据: \n" + dishNutritionData);

        String mealTypeName = getMealTypeName(mealType);
        System.out.println("餐类型名称: " + mealTypeName);
        
        String prompt = String.format(
            "你是一位专业营养师，现在需要根据以下可选菜品为用户设计一份%s套餐。\n\n" +
            "可选菜品及营养数据（名称：热量kcal，蛋白质g，脂肪g，碳水g，钠mg，膳食纤维g）：\n%s\n\n" +
            "设计规则：\n" +
            "1. 仅从上述列表中选择3-4道菜，不得推荐列表外的菜品\n" +
            "2. %s的具体营养要求：\n" +
            "   - 减脂餐：每道菜热量≤150kcal，总热量≤500kcal，优先选低热量蔬菜和粗粮主食，膳食纤维越高越好\n" +
            "   - 增肌餐：每道菜蛋白质≥15g或选蛋白质最高的肉类，总蛋白质≥40g，脂肪控制在合理范围\n" +
            "   - 低糖餐：每道菜碳水≤15g，总碳水≤50g，优先选碳水最低的菜品，避免高糖主食\n" +
            "   - 养胃餐/清淡餐：选择汤类、粥类、蒸蛋、绿叶蔬菜，钠含量≤300mg，避免油腻辛辣\n" +
            "   - 清淡餐：钠含量≤300mg，脂肪≤10g，选择清蒸、清炒、水煮类菜品\n" +
            "3. 套餐结构：必须包含1种主食 + 2-3种菜肴（蛋白质类+蔬菜类）\n" +
            "4. 营养均衡：兼顾蛋白质、碳水、蔬菜的搭配，膳食纤维≥5g\n" +
            "5. 健康原则：优先选择低钠、高纤维的菜品\n" +
            "6. 输出格式：仅用中文分号分隔菜品名称，例如：白米饭；番茄炒蛋；蒜蓉菠菜\n" +
            "7. 不要输出任何解释文字，只输出推荐的菜品名称",
            mealTypeName,
            dishNutritionData.toString(),
            mealTypeName
        );

        String aiResponse = aiService.chat(prompt);
        System.out.println("AI响应: " + aiResponse);
        
        List<String> recommendedDishNames = parseMealRecommendation(aiResponse);
        System.out.println("解析出的菜品名称: " + recommendedDishNames);
        
        List<Dish> result = new ArrayList<>();

        for (String name : recommendedDishNames) {
            for (Dish dish : availableDishes) {
                if (dish.getName() != null && dish.getName().equals(name.trim())) {
                    result.add(dish);
                    System.out.println("匹配到菜品: " + dish.getName());
                    break;
                }
            }
        }

        System.out.println("AI推荐匹配结果: " + result.size() + " 道菜品");

        if (result.isEmpty()) {
            System.out.println("AI推荐为空，使用回退推荐");
            result = getFallbackRecommendation(availableDishes, mealType);
        }

        if (result.size() < 3) {
            List<Dish> finalResult = result;
            List<Dish> remaining = availableDishes.stream()
                    .filter(d -> !finalResult.contains(d))
                    .limit(3 - result.size())
                    .collect(Collectors.toList());
            result.addAll(remaining);
        }

        // 为智能配餐菜品计算健康评级
        fillHealthRating(result);
        return result;
    }

    private List<Dish> getSpicyDishes(List<Dish> dishes) {
        return dishes.stream()
                .filter(d -> {
                    String name = d.getName() != null ? d.getName().toLowerCase() : "";
                    String ingredients = d.getIngredients() != null ? d.getIngredients().toLowerCase() : "";
                    return name.contains("辣") || name.contains("麻") || name.contains("椒") ||
                           ingredients.contains("辣") || ingredients.contains("麻") || ingredients.contains("椒");
                })
                .collect(Collectors.toList());
    }

    private String getMealTypeName(String mealType) {
        switch (mealType.toLowerCase()) {
            case "low-calorie":
            case "减脂":
                return "减脂餐";
            case "high-protein":
            case "增肌":
                return "增肌餐";
            case "low-sugar":
            case "low-carb":
            case "低糖":
                return "低糖餐";
            case "stomach-care":
            case "gentle":
            case "养胃":
                return "养胃餐";
            case "light":
            case "清淡":
                return "清淡餐";
            default:
                return "营养均衡餐";
        }
    }

    private List<String> parseMealRecommendation(String aiResponse) {
        List<String> result = new ArrayList<>();
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            return result;
        }
        String[] parts = aiResponse.split("；|;|,|，|\\s+");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty() && trimmed.length() <= 10) {
                result.add(trimmed);
            }
        }
        return result.stream().distinct().limit(4).collect(Collectors.toList());
    }

    private List<Dish> getFallbackRecommendation(List<Dish> availableDishes, String mealType) {
        List<Dish> result = new ArrayList<>();
        switch (mealType.toLowerCase()) {
            case "low-calorie":
            case "减脂":
                result = availableDishes.stream()
                        .filter(d -> d.getCalories() != null && d.getCalories().compareTo(new BigDecimal("300")) <= 0)
                        .sorted((d1, d2) -> d1.getCalories().compareTo(d2.getCalories()))
                        .limit(4)
                        .collect(Collectors.toList());
                break;
            case "high-protein":
            case "增肌":
                result = availableDishes.stream()
                        .filter(d -> d.getProtein() != null && d.getProtein().compareTo(new BigDecimal("15")) >= 0)
                        .sorted((d1, d2) -> d2.getProtein().compareTo(d1.getProtein()))
                        .limit(4)
                        .collect(Collectors.toList());
                break;
            case "low-sugar":
            case "low-carb":
            case "低糖":
            case "gentle":
            case "stomach-care":
            case "养胃":
            case "light":
            case "清淡":
                result = availableDishes.stream()
                        .filter(d -> {
                            String name = d.getName() != null ? d.getName().toLowerCase() : "";
                            return !name.contains("辣") && !name.contains("麻") && !name.contains("椒");
                        })
                        .sorted((d1, d2) -> d1.getCalories().compareTo(d2.getCalories()))
                        .limit(4)
                        .collect(Collectors.toList());
                break;
            default:
                result = availableDishes.stream()
                        .sorted((d1, d2) -> d2.getSalesCount().compareTo(d1.getSalesCount()))
                        .limit(4)
                        .collect(Collectors.toList());
        }
        return result;
    }
}
