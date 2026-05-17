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

    @Override
    public List<Dish> getAllDishes() {
        return baseMapper.selectAllWithNutrition();
    }

    @Override
    public List<Dish> getDishesByCategory(Long categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId)
               .eq(Dish::getIsShelf, 1);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<Dish> getDishesByFloor(Integer floor) {
        return baseMapper.selectByFloor(floor);
    }

    @Override
    public List<Dish> getDishesByWindow(Long windowId) {
        return baseMapper.selectByWindow(windowId);
    }

    @Override
    public List<Dish> getDishesByCategoryAndWindow(Long categoryId, Long windowId) {
        return baseMapper.selectByCategoryAndWindow(categoryId, windowId);
    }

    @Override
    public List<Dish> searchDishes(String keyword) {
        return baseMapper.selectByKeyword(keyword);
    }

    @Override
    public List<Dish> searchDishesByCategory(Long categoryId, String keyword) {
        return baseMapper.selectByCategoryAndKeyword(categoryId, keyword);
    }

    @Override
    public List<Dish> searchDishesByWindow(Long windowId, String keyword) {
        return baseMapper.selectByWindowAndKeyword(windowId, keyword);
    }

    @Override
    public List<Dish> searchDishesByCategoryAndWindow(Long categoryId, Long windowId, String keyword) {
        return baseMapper.selectByCategoryWindowAndKeyword(categoryId, windowId, keyword);
    }

    @Override
    public PageResult<Dish> getDishesPage(Integer pageNum, Integer pageSize) {
        Page<Dish> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getIsShelf, 1)
               .orderByDesc(Dish::getSalesCount);
        this.baseMapper.selectPage(page, wrapper);
        return new PageResult<>(page.getTotal(), page.getRecords(), 
                               page.getCurrent(), page.getSize());
    }

    @Override
    public Dish getDishDetail(Long dishId) {
        return this.baseMapper.selectById(dishId);
    }

    @Override
    public Map<String, Object> getDishWithNutrition(Long dishId) {
        Map<String, Object> result = new HashMap<>();
        Dish dish = this.baseMapper.selectById(dishId);
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