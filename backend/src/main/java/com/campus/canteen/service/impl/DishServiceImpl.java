package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private NutritionMapper nutritionMapper;

    // 菜品名称到图片文件的映射
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

    // 自动填充图片路径
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

    @Override
    public List<Dish> getAllDishes() {
        List<Dish> dishes = baseMapper.selectAllWithNutrition();
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> getDishesByCategory(Long categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId)
               .eq(Dish::getIsShelf, 1);
        List<Dish> dishes = this.baseMapper.selectList(wrapper);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> getDishesByFloor(Integer floor) {
        List<Dish> dishes = baseMapper.selectByFloor(floor);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> getDishesByWindow(Long windowId) {
        List<Dish> dishes = baseMapper.selectByWindow(windowId);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> getDishesByCategoryAndWindow(Long categoryId, Long windowId) {
        List<Dish> dishes = baseMapper.selectByCategoryAndWindow(categoryId, windowId);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> searchDishes(String keyword) {
        List<Dish> dishes = baseMapper.selectByKeyword(keyword);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> searchDishesByCategory(Long categoryId, String keyword) {
        List<Dish> dishes = baseMapper.selectByCategoryAndKeyword(categoryId, keyword);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> searchDishesByWindow(Long windowId, String keyword) {
        List<Dish> dishes = baseMapper.selectByWindowAndKeyword(windowId, keyword);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public List<Dish> searchDishesByCategoryAndWindow(Long categoryId, Long windowId, String keyword) {
        List<Dish> dishes = baseMapper.selectByCategoryWindowAndKeyword(categoryId, windowId, keyword);
        fillImageUrl(dishes);
        return dishes;
    }

    @Override
    public PageResult<Dish> getDishesPage(Integer pageNum, Integer pageSize) {
        Page<Dish> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getIsShelf, 1)
               .orderByDesc(Dish::getSalesCount);
        this.baseMapper.selectPage(page, wrapper);
        fillImageUrl(page.getRecords());
        return new PageResult<>(page.getTotal(), page.getRecords(), 
                               page.getCurrent(), page.getSize());
    }

    @Override
    public Dish getDishDetail(Long dishId) {
        Dish dish = this.baseMapper.selectById(dishId);
        fillImageUrl(dish);
        return dish;
    }

    @Override
    public Map<String, Object> getDishWithNutrition(Long dishId) {
        Map<String, Object> result = new HashMap<>();
        Dish dish = this.baseMapper.selectById(dishId);
        fillImageUrl(dish);
        Nutrition nutrition;
        nutrition = nutritionMapper.selectNutritionByDishId(dishId);
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