package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.common.PageResult;
import com.campus.canteen.entity.Dish;

import java.util.List;
import java.util.Map;

public interface DishService extends IService<Dish> {
    List<Dish> getAllDishes();
    List<Dish> getDishesByCategory(Long categoryId);
    List<Dish> getDishesByFloor(Integer floor);
    List<Dish> getDishesByWindow(Long windowId);
    List<Dish> getDishesByCategoryAndWindow(Long categoryId, Long windowId);
    List<Dish> searchDishes(String keyword);
    List<Dish> searchDishesByCategory(Long categoryId, String keyword);
    List<Dish> searchDishesByWindow(Long windowId, String keyword);
    List<Dish> searchDishesByCategoryAndWindow(Long categoryId, Long windowId, String keyword);
    PageResult<Dish> getDishesPage(Integer pageNum, Integer pageSize);
    PageResult<Dish> getDishesPageWithFilter(Long categoryId, Long windowId, String keyword, Integer floor, Integer pageNum, Integer pageSize);
    Dish getDishDetail(Long dishId);
    Map<String, Object> getDishWithNutrition(Long dishId);
    boolean addDish(Dish dish);
    boolean updateDish(Dish dish);
    boolean deleteDish(Long dishId);
    boolean updateStock(Long dishId, Integer quantity);
}