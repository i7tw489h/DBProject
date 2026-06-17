package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.common.PageResult;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Nutrition;
import com.campus.canteen.mapper.DishMapper;
import com.campus.canteen.mapper.NutritionMapper;
import com.campus.canteen.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private NutritionMapper nutritionMapper;

    private static final Map<String, String> DISH_IMAGE_MAP = new HashMap<>();
    static {
        DISH_IMAGE_MAP.put("宫保鸡丁", "/images/dishes/1.jpg");
        DISH_IMAGE_MAP.put("鱼香肉丝", "/images/dishes/2.jpg");
        DISH_IMAGE_MAP.put("红烧肉", "/images/dishes/3.jpg");
        DISH_IMAGE_MAP.put("糖醋里脊", "/images/dishes/4.jpg");
        DISH_IMAGE_MAP.put("番茄炒蛋", "/images/dishes/番茄炒蛋.jpg");
        DISH_IMAGE_MAP.put("蒜蓉菠菜", "/images/dishes/蒜蓉菠菜.jpg");
        DISH_IMAGE_MAP.put("白米饭", "/images/dishes/白米饭.jpg");
        DISH_IMAGE_MAP.put("清炒西蓝花", "/images/dishes/清炒西蓝花.jpg");
        DISH_IMAGE_MAP.put("麻婆豆腐", "/images/dishes/麻婆豆腐.jpg");
        DISH_IMAGE_MAP.put("酸辣汤", "/images/dishes/酸辣汤.jpg");
        DISH_IMAGE_MAP.put("排骨汤", "/images/dishes/排骨汤.jpg");
        DISH_IMAGE_MAP.put("小米粥", "/images/dishes/小米粥.jpg");
        DISH_IMAGE_MAP.put("豆沙包", "/images/dishes/豆沙包.jpg");
        DISH_IMAGE_MAP.put("花卷", "/images/dishes/花卷.jpg");
    }

    private void fillImageUrl(Dish dish) {
        if (dish != null && (dish.getImageUrl() == null || dish.getImageUrl().isEmpty())) {
            String imageUrl = DISH_IMAGE_MAP.get(dish.getName());
            if (imageUrl != null) {
                dish.setImageUrl(imageUrl);
            }
        }
    }

    private void fillImageUrl(List<Dish> dishes) {
        if (dishes != null) {
            for (Dish dish : dishes) {
                fillImageUrl(dish);
            }
        }
    }

    private void fillNutrition(List<Dish> dishes) {
        if (dishes == null || dishes.isEmpty()) {
            return;
        }
        List<Long> dishIds = dishes.stream()
                .map(Dish::getDishId)
                .collect(Collectors.toList());
        LambdaQueryWrapper<Nutrition> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Nutrition::getDishId, dishIds);
        List<Nutrition> nutritionList = nutritionMapper.selectList(wrapper);
        Map<Long, Nutrition> nutritionMap = nutritionList.stream()
                .collect(Collectors.toMap(Nutrition::getDishId, n -> n, (a, b) -> a));
        for (Dish dish : dishes) {
            // 如果视图已经返回了 health_rating，则不覆盖（这是数据库函数计算的结果）
            Nutrition nutrition = nutritionMap.get(dish.getDishId());
            if (nutrition != null) {
                if (dish.getCalories() == null) dish.setCalories(nutrition.getCalories());
                if (dish.getProtein() == null) dish.setProtein(nutrition.getProtein());
                if (dish.getFat() == null) dish.setFat(nutrition.getFat());
                if (dish.getCarbs() == null) dish.setCarbs(nutrition.getCarbs());
                if (dish.getSodium() == null) dish.setSodium(nutrition.getSodium());
                if (dish.getFiber() == null) dish.setFiber(nutrition.getFiber());
            }
        }
    }

    private List<Dish> prepareDishes(List<Dish> dishes) {
        fillImageUrl(dishes);
        fillNutrition(dishes);
        fillHealthRating(dishes);
        return dishes;
    }

    /**
     * 调用存储函数 fn_calc_health_rating 为每个菜品计算健康评级
     */
    private void fillHealthRating(List<Dish> dishes) {
        if (dishes == null || dishes.isEmpty()) {
            return;
        }
        for (Dish dish : dishes) {
            try {
                String rating = baseMapper.calcHealthRating(
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
    public List<Dish> getAllDishes() {
        return prepareDishes(baseMapper.selectAllWithNutrition());
    }

    @Override
    public List<Dish> getDishesByCategory(Long categoryId) {
        return prepareDishes(baseMapper.selectByCategoryWithNutrition(categoryId));
    }

    @Override
    public List<Dish> getDishesByFloor(Integer floor) {
        return prepareDishes(baseMapper.selectByFloor(floor));
    }

    @Override
    public List<Dish> getDishesByWindow(Long windowId) {
        return prepareDishes(baseMapper.selectByWindowWithNutrition(windowId));
    }

    @Override
    public List<Dish> getDishesByCategoryAndWindow(Long categoryId, Long windowId) {
        return prepareDishes(baseMapper.selectByCategoryAndWindowWithNutrition(categoryId, windowId));
    }

    @Override
    public List<Dish> searchDishes(String keyword) {
        return prepareDishes(baseMapper.selectByKeyword(keyword));
    }

    @Override
    public List<Dish> searchDishesByCategory(Long categoryId, String keyword) {
        return prepareDishes(baseMapper.selectByCategoryAndKeyword(categoryId, keyword));
    }

    @Override
    public List<Dish> searchDishesByWindow(Long windowId, String keyword) {
        return prepareDishes(baseMapper.selectByWindowAndKeyword(windowId, keyword));
    }

    @Override
    public List<Dish> searchDishesByCategoryAndWindow(Long categoryId, Long windowId, String keyword) {
        return prepareDishes(baseMapper.selectByCategoryWindowAndKeyword(categoryId, windowId, keyword));
    }

    @Override
    public PageResult<Dish> getDishesPage(Integer pageNum, Integer pageSize) {
        Page<Dish> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getIsShelf, 1)
               .orderByDesc(Dish::getSalesCount);
        this.baseMapper.selectPage(page, wrapper);
        prepareDishes(page.getRecords());
        return new PageResult<>(page.getTotal(), page.getRecords(),
                               page.getCurrent(), page.getSize());
    }

    @Override
    public PageResult<Dish> getDishesPageWithFilter(Long categoryId, Long windowId, String keyword, Integer floor, Integer pageNum, Integer pageSize) {
        Page<Dish> page = new Page<>(pageNum, pageSize);
        // 使用自定义视图分页查询，让 health_rating 字段可以返回
        IPage<Dish> dishPage = baseMapper.selectPageWithFilter(page, 
                categoryId != null && categoryId > 0 ? categoryId : null,
                windowId != null && windowId > 0 ? windowId : null,
                (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null);
        prepareDishes(dishPage.getRecords());
        return new PageResult<>(dishPage.getTotal(), dishPage.getRecords(),
                               dishPage.getCurrent(), dishPage.getSize());
    }

    @Override
    public Dish getDishDetail(Long dishId) {
        Dish dish = this.baseMapper.selectById(dishId);
        if (dish != null) {
            fillImageUrl(dish);
            fillNutrition(Collections.singletonList(dish));
        }
        return dish;
    }

    @Override
    public Map<String, Object> getDishWithNutrition(Long dishId) {
        Map<String, Object> result = new HashMap<>();
        Dish dish = getDishDetail(dishId);
        Nutrition nutrition = nutritionMapper.selectNutritionByDishId(dishId);
        result.put("dish", dish);
        result.put("nutrition", nutrition);
        return result;
    }

    @Override
    @Transactional
    public boolean addDish(Dish dish) {
        this.baseMapper.insert(dish);
        return true;
    }

    @Override
    @Transactional
    public boolean updateDish(Dish dish) {
        this.baseMapper.updateById(dish);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteDish(Long dishId) {
        Dish dish = new Dish();
        dish.setDishId(dishId);
        dish.setIsShelf(0);
        this.baseMapper.updateById(dish);
        return true;
    }

    @Override
    @Transactional
    public boolean updateStock(Long dishId, Integer quantity) {
        Dish dish = this.baseMapper.selectById(dishId);
        if (dish != null) {
            dish.setStock(dish.getStock() - quantity);
            this.baseMapper.updateById(dish);
        }
        return true;
    }
}
