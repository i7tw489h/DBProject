package com.campus.canteen.service.impl;

import com.campus.canteen.entity.HealthEvaluation;
import com.campus.canteen.entity.Nutrition;
import com.campus.canteen.entity.Order;
import com.campus.canteen.entity.OrderItem;
import com.campus.canteen.entity.User;
import com.campus.canteen.mapper.NutritionMapper;
import com.campus.canteen.mapper.OrderItemMapper;
import com.campus.canteen.mapper.OrderMapper;
import com.campus.canteen.mapper.UserMapper;
import com.campus.canteen.service.AIService;
import com.campus.canteen.service.NutritionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NutritionServiceImpl implements NutritionService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private NutritionMapper nutritionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AIService aiService;

    @Override
    public Map<String, BigDecimal> getTodayNutrition(Long userId) {
        System.out.println("=== getTodayNutrition 开始 ===");
        System.out.println("用户ID: " + userId);
        
        Map<String, BigDecimal> nutrition = new HashMap<>();
        nutrition.put("calories", BigDecimal.ZERO);
        nutrition.put("protein", BigDecimal.ZERO);
        nutrition.put("fat", BigDecimal.ZERO);
        nutrition.put("carbs", BigDecimal.ZERO);
        nutrition.put("sodium", BigDecimal.ZERO);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        QueryWrapper<Order> orderQuery = new QueryWrapper<>();
        orderQuery.eq("user_id", userId)
                .ge("created_at", todayStart)
                .le("created_at", todayEnd)
                .eq("status", 4);

        List<Order> orders = orderMapper.selectList(orderQuery);
        System.out.println("今日订单数量: " + orders.size());

        for (Order order : orders) {
            System.out.println("处理订单: " + order.getOrderId());
            QueryWrapper<OrderItem> itemQuery = new QueryWrapper<>();
            itemQuery.eq("order_id", order.getOrderId());
            List<OrderItem> items = orderItemMapper.selectList(itemQuery);
            System.out.println("订单项数量: " + items.size());

            for (OrderItem item : items) {
                System.out.println("处理订单项: dishId=" + item.getDishId() + ", quantity=" + item.getQuantity());
                Nutrition dishNutrition = nutritionMapper.selectNutritionByDishId(item.getDishId());
                System.out.println("查询到的营养数据: " + (dishNutrition != null ? "有" : "无"));
                if (dishNutrition != null) {
                    int quantity = item.getQuantity();
                    if (dishNutrition.getCalories() != null) {
                        nutrition.merge("calories", dishNutrition.getCalories().multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);
                    }
                    if (dishNutrition.getProtein() != null) {
                        nutrition.merge("protein", dishNutrition.getProtein().multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);
                    }
                    if (dishNutrition.getFat() != null) {
                        nutrition.merge("fat", dishNutrition.getFat().multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);
                    }
                    if (dishNutrition.getCarbs() != null) {
                        nutrition.merge("carbs", dishNutrition.getCarbs().multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);
                    }
                    if (dishNutrition.getSodium() != null) {
                        nutrition.merge("sodium", dishNutrition.getSodium().multiply(BigDecimal.valueOf(quantity)), BigDecimal::add);
                    }
                }
            }
        }

        for (Map.Entry<String, BigDecimal> entry : nutrition.entrySet()) {
            nutrition.put(entry.getKey(), entry.getValue().setScale(2, RoundingMode.HALF_UP));
        }

        System.out.println("=== getTodayNutrition 完成 ===");
        System.out.println("营养数据: " + nutrition);
        return nutrition;
    }

    @Override
    public List<Map<String, Object>> getHistoryNutrition(Long userId, int days) {
        List<Map<String, Object>> history = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> dayNutrition = new HashMap<>();
            dayNutrition.put("date", date.toString());
            dayNutrition.put("calories", BigDecimal.ZERO);
            dayNutrition.put("protein", BigDecimal.ZERO);
            dayNutrition.put("fat", BigDecimal.ZERO);
            dayNutrition.put("carbs", BigDecimal.ZERO);

            QueryWrapper<Order> orderQuery = new QueryWrapper<>();
            orderQuery.eq("user_id", userId)
                    .ge("created_at", dayStart)
                    .le("created_at", dayEnd)
                    .eq("status", 4);

            List<Order> orders = orderMapper.selectList(orderQuery);

            for (Order order : orders) {
                QueryWrapper<OrderItem> itemQuery = new QueryWrapper<>();
                itemQuery.eq("order_id", order.getOrderId());
                List<OrderItem> items = orderItemMapper.selectList(itemQuery);

                for (OrderItem item : items) {
                    Nutrition dishNutrition = nutritionMapper.selectNutritionByDishId(item.getDishId());
                    if (dishNutrition != null) {
                        int quantity = item.getQuantity();
                        BigDecimal calories = (BigDecimal) dayNutrition.get("calories");
                        if (dishNutrition.getCalories() != null) {
                            dayNutrition.put("calories", calories.add(dishNutrition.getCalories().multiply(BigDecimal.valueOf(quantity))));
                        }
                        BigDecimal protein = (BigDecimal) dayNutrition.get("protein");
                        if (dishNutrition.getProtein() != null) {
                            dayNutrition.put("protein", protein.add(dishNutrition.getProtein().multiply(BigDecimal.valueOf(quantity))));
                        }
                        BigDecimal fat = (BigDecimal) dayNutrition.get("fat");
                        if (dishNutrition.getFat() != null) {
                            dayNutrition.put("fat", fat.add(dishNutrition.getFat().multiply(BigDecimal.valueOf(quantity))));
                        }
                        BigDecimal carbs = (BigDecimal) dayNutrition.get("carbs");
                        if (dishNutrition.getCarbs() != null) {
                            dayNutrition.put("carbs", carbs.add(dishNutrition.getCarbs().multiply(BigDecimal.valueOf(quantity))));
                        }
                    }
                }
            }

            dayNutrition.put("calories", ((BigDecimal) dayNutrition.get("calories")).setScale(0, RoundingMode.HALF_UP));
            dayNutrition.put("protein", ((BigDecimal) dayNutrition.get("protein")).setScale(0, RoundingMode.HALF_UP));
            dayNutrition.put("fat", ((BigDecimal) dayNutrition.get("fat")).setScale(0, RoundingMode.HALF_UP));
            dayNutrition.put("carbs", ((BigDecimal) dayNutrition.get("carbs")).setScale(0, RoundingMode.HALF_UP));
            history.add(dayNutrition);
        }

        return history;
    }

    @Override
    public Map<String, BigDecimal> getNutritionTargets(Long userId) {
        User user = userMapper.selectById(userId);
        Map<String, BigDecimal> targets = new HashMap<>();
        targets.put("calories", calculateTargetCalories(user).setScale(0, RoundingMode.HALF_UP));
        targets.put("protein", calculateTargetProtein(user).setScale(0, RoundingMode.HALF_UP));
        targets.put("fat", calculateTargetFat(user).setScale(0, RoundingMode.HALF_UP));
        targets.put("carbs", calculateTargetCarbs(user).setScale(0, RoundingMode.HALF_UP));
        return targets;
    }

    @Override
    public HealthEvaluation evaluateHealth(Long userId) {
        System.out.println("=== evaluateHealth 开始 ===");
        System.out.println("用户ID: " + userId);
        
        Map<String, BigDecimal> today = getTodayNutrition(userId);
        User user = userMapper.selectById(userId);
        System.out.println("查询到的用户: " + (user != null ? "有" : "无"));

        String dietGoal = "";
        if (user != null && user.getDietGoal() != null) {
            switch (user.getDietGoal()) {
                case 1: dietGoal = "减脂"; break;
                case 2: dietGoal = "增肌"; break;
                case 3: dietGoal = "养胃"; break;
                default: dietGoal = "保持健康";
            }
        }

        String prompt = String.format(
            "请作为专业营养师，分析以下用户的今日营养摄入情况：\n\n" +
            "【用户信息】\n" +
            "性别：%s\n" +
            "年龄：%s\n" +
            "身高：%scm\n" +
            "体重：%skg\n" +
            "饮食目标：%s\n\n" +
            "【今日营养摄入】\n" +
            "热量：%skcal\n" +
            "蛋白质：%sg\n" +
            "脂肪：%sg\n" +
            "碳水化合物：%sg\n\n" +
            "请按照以下格式输出分析结果：\n" +
            "评分：[0-100分]\n" +
            "状态：[优秀/良好/一般/较差]\n" +
            "优点：[用分号分隔，如：蛋白质摄入充足；碳水比例合理]\n" +
            "问题：[用分号分隔，如：热量超标；脂肪摄入偏高]\n" +
            "建议：[详细的健康建议]",
            user != null && user.getGender() != null && user.getGender() == 1 ? "男" : "女",
            user != null && user.getAge() != null ? user.getAge() : "未知",
            user != null && user.getHeight() != null ? user.getHeight() : "未知",
            user != null && user.getWeight() != null ? user.getWeight() : "未知",
            dietGoal,
            today.get("calories"),
            today.get("protein"),
            today.get("fat"),
            today.get("carbs")
        );

        System.out.println("发送给AI的prompt: " + prompt);
        String aiResponse = aiService.chat(prompt);
        System.out.println("AI返回的响应: " + aiResponse);
        
        HealthEvaluation result = parseAIResponse(aiResponse, today, user);
        System.out.println("=== evaluateHealth 完成 ===");
        return result;
    }

    private HealthEvaluation parseAIResponse(String response, Map<String, BigDecimal> nutrition, User user) {
        HealthEvaluation evaluation = new HealthEvaluation();
        List<String> issues = new ArrayList<>();
        List<String> positives = new ArrayList<>();

        int score = 75;
        String status = "良好";
        String advice = "根据您的营养摄入分析，以下是专业建议：";

        System.out.println("=== 开始解析AI响应 ===");
        System.out.println("AI响应内容: " + response);
        System.out.println("响应长度: " + response.length());
        
        if (response.contains("评分：")) {
            try {
                int startIdx = response.indexOf("评分：") + 3;
                int endIdx = response.indexOf("\n", startIdx);
                if (endIdx == -1) endIdx = response.length();
                String scoreStr = response.substring(startIdx, endIdx).trim();
                System.out.println("提取到评分字符串: '" + scoreStr + "'");
                
                // 移除"分"字和其他非数字字符，只保留数字
                scoreStr = scoreStr.replaceAll("[^0-9]", "");
                System.out.println("清理后的评分字符串: '" + scoreStr + "'");
                
                if (!scoreStr.isEmpty()) {
                    score = Integer.parseInt(scoreStr);
                    System.out.println("解析出评分: " + score);
                } else {
                    System.out.println("清理后评分字符串为空，使用默认值75");
                    score = 75;
                }
            } catch (Exception e) {
                System.out.println("解析评分失败，使用默认值75: " + e.getMessage());
                score = 75;
            }
        } else {
            System.out.println("AI响应中没有找到'评分：'，使用默认值75");
        }

        if (response.contains("状态：")) {
            try {
                status = response.substring(response.indexOf("状态：") + 3);
                status = status.substring(0, status.indexOf("\n")).trim();
            } catch (Exception e) {
                status = getStatusByScore(score);
            }
        } else {
            status = getStatusByScore(score);
        }

        if (response.contains("优点：")) {
            try {
                String positivesStr = response.substring(response.indexOf("优点：") + 3);
                if (positivesStr.contains("\n")) {
                    positivesStr = positivesStr.substring(0, positivesStr.indexOf("\n")).trim();
                }
                String[] parts = positivesStr.split("；|;");
                for (String part : parts) {
                    if (!part.trim().isEmpty()) {
                        positives.add("✨ " + part.trim());
                    }
                }
            } catch (Exception e) {
                positives.addAll(generateMockPositives(nutrition, user));
            }
        } else {
            positives.addAll(generateMockPositives(nutrition, user));
        }

        if (response.contains("问题：")) {
            try {
                String issuesStr = response.substring(response.indexOf("问题：") + 3);
                if (issuesStr.contains("\n")) {
                    issuesStr = issuesStr.substring(0, issuesStr.indexOf("\n")).trim();
                }
                String[] parts = issuesStr.split("；|;");
                for (String part : parts) {
                    if (!part.trim().isEmpty()) {
                        issues.add("⚠️ " + part.trim());
                    }
                }
            } catch (Exception e) {
                issues.addAll(generateMockIssues(nutrition, user));
            }
        } else {
            issues.addAll(generateMockIssues(nutrition, user));
        }

        if (response.contains("建议：")) {
            try {
                advice = response.substring(response.indexOf("建议：") + 3).trim();
            } catch (Exception e) {
                advice = generateMockAdvice(status, issues);
            }
        } else {
            advice = generateMockAdvice(status, issues);
        }

        evaluation.setScore(Math.max(0, Math.min(100, score)));
        evaluation.setStatus(status);
        evaluation.setAdvice(advice);
        evaluation.setIssues(issues);
        evaluation.setPositives(positives);

        return evaluation;
    }

    private String getStatusByScore(int score) {
        if (score >= 90) return "优秀";
        if (score >= 70) return "良好";
        if (score >= 50) return "一般";
        return "较差";
    }

    private List<String> generateMockPositives(Map<String, BigDecimal> nutrition, User user) {
        List<String> positives = new ArrayList<>();
        BigDecimal protein = nutrition.get("protein");
        BigDecimal carbs = nutrition.get("carbs");

        if (protein != null && protein.compareTo(BigDecimal.valueOf(60)) >= 0) {
            positives.add("💪 蛋白质摄入充足");
        }
        if (carbs != null && carbs.compareTo(BigDecimal.valueOf(150)) >= 0 && carbs.compareTo(BigDecimal.valueOf(300)) <= 0) {
            positives.add("🍞 碳水化合物比例合理");
        }
        if (positives.isEmpty()) {
            positives.add("✨ 饮食整体较为均衡");
        }
        return positives;
    }

    private List<String> generateMockIssues(Map<String, BigDecimal> nutrition, User user) {
        List<String> issues = new ArrayList<>();
        BigDecimal calories = nutrition.get("calories");
        BigDecimal fat = nutrition.get("fat");

        if (calories != null && calories.compareTo(BigDecimal.valueOf(2500)) > 0) {
            issues.add("热量超标，建议减少高油高糖食物");
        } else if (calories != null && calories.compareTo(BigDecimal.valueOf(1500)) < 0) {
            issues.add("热量摄入不足，建议适当增加主食");
        }
        if (fat != null && fat.compareTo(BigDecimal.valueOf(80)) > 0) {
            issues.add("脂肪摄入偏高，建议清淡饮食");
        }
        return issues;
    }

    private String generateMockAdvice(String status, List<String> issues) {
        String baseAdvice;
        switch (status) {
            case "优秀":
                baseAdvice = "🎉 太棒了！您今天的饮食非常健康均衡，继续保持！";
                break;
            case "良好":
                baseAdvice = "👍 不错！您今天的饮食整体健康，继续努力！";
                break;
            case "一般":
                baseAdvice = "💪 还需努力！根据以上建议调整饮食，会变得更健康！";
                break;
            default:
                baseAdvice = "⚠️ 需要调整！建议咨询营养师制定个性化饮食方案。";
        }

        if (!issues.isEmpty()) {
            baseAdvice += " 具体建议：" + String.join("；", issues);
        }

        return baseAdvice;
    }

    private BigDecimal calculateTargetCalories(User user) {
        if (user == null) return BigDecimal.valueOf(2000);

        double bmr;
        if (user.getGender() != null && user.getGender() == 1) {
            bmr = 10 * (user.getWeight() != null ? user.getWeight().doubleValue() : 60) +
                  6.25 * (user.getHeight() != null ? user.getHeight().doubleValue() : 170) -
                  5 * (user.getAge() != null ? user.getAge() : 20) + 5;
        } else {
            bmr = 10 * (user.getWeight() != null ? user.getWeight().doubleValue() : 55) +
                  6.25 * (user.getHeight() != null ? user.getHeight().doubleValue() : 160) -
                  5 * (user.getAge() != null ? user.getAge() : 20) - 161;
        }

        double activityFactor = 1.2;
        if (user.getDietGoal() != null) {
            switch (user.getDietGoal()) {
                case 1: return BigDecimal.valueOf(bmr * activityFactor * 0.85);
                case 2: return BigDecimal.valueOf(bmr * activityFactor * 1.2);
                case 3: return BigDecimal.valueOf(bmr * activityFactor * 0.9);
                default: return BigDecimal.valueOf(bmr * activityFactor);
            }
        }
        return BigDecimal.valueOf(bmr * activityFactor);
    }

    private BigDecimal calculateTargetProtein(User user) {
        double weight = user != null && user.getWeight() != null ? user.getWeight().doubleValue() : 60;
        return BigDecimal.valueOf(weight * 1.2);
    }

    private BigDecimal calculateTargetFat(User user) {
        BigDecimal calories = calculateTargetCalories(user);
        return calories.multiply(BigDecimal.valueOf(0.25)).divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTargetCarbs(User user) {
        BigDecimal calories = calculateTargetCalories(user);
        return calories.multiply(BigDecimal.valueOf(0.5)).divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
    }
}
