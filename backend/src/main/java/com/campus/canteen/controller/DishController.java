package com.campus.canteen.controller;

import com.campus.canteen.common.PageResult;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Category;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Window;
import com.campus.canteen.service.CategoryService;
import com.campus.canteen.service.DishService;
import com.campus.canteen.service.WindowService;
import com.campus.canteen.service.UserRestrictionsService;
import com.campus.canteen.entity.UserRestrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WindowService windowService;

    @Autowired
    private UserRestrictionsService userRestrictionsService;

    @GetMapping("/categories")
    public Result<?> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }

    @GetMapping("/windows")
    public Result<?> getWindows() {
        List<Window> windows = windowService.getAllWindows();
        return Result.success(windows);
    }

    @GetMapping("/windows/{floor}")
    public Result<?> getWindowsByFloor(@PathVariable Integer floor) {
        List<Window> windows = windowService.getWindowsByFloor(floor);
        return Result.success(windows);
    }

    @GetMapping("/dishes")
    public Result<?> getDishes(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long windowId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer floor,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "100") Integer pageSize) {
        // 清理空字符串
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        
        // 处理组合筛选
        if (keyword != null && categoryId != null && windowId != null) {
            List<Dish> dishes = dishService.searchDishesByCategoryAndWindow(categoryId, windowId, keyword);
            return Result.success(dishes);
        } else if (keyword != null && categoryId != null) {
            List<Dish> dishes = dishService.searchDishesByCategory(categoryId, keyword);
            return Result.success(dishes);
        } else if (keyword != null && windowId != null) {
            List<Dish> dishes = dishService.searchDishesByWindow(windowId, keyword);
            return Result.success(dishes);
        } else if (keyword != null) {
            List<Dish> dishes = dishService.searchDishes(keyword);
            return Result.success(dishes);
        } else if (categoryId != null && windowId != null) {
            List<Dish> dishes = dishService.getDishesByCategoryAndWindow(categoryId, windowId);
            return Result.success(dishes);
        } else if (categoryId != null) {
            List<Dish> dishes = dishService.getDishesByCategory(categoryId);
            return Result.success(dishes);
        } else if (windowId != null) {
            List<Dish> dishes = dishService.getDishesByWindow(windowId);
            return Result.success(dishes);
        } else if (floor != null) {
            List<Dish> dishes = dishService.getDishesByFloor(floor);
            return Result.success(dishes);
        } else {
            PageResult<Dish> page = dishService.getDishesPage(pageNum, pageSize);
            return Result.success(page);
        }
    }

    @GetMapping("/dish/{dishId}")
    public Result<?> getDishDetail(@PathVariable Long dishId) {
        Map<String, Object> result = dishService.getDishWithNutrition(dishId);
        return Result.success(result);
    }

    @GetMapping("/dishes/{dishId}")
    public Result<?> getDishById(@PathVariable Long dishId) {
        Map<String, Object> result = dishService.getDishWithNutrition(dishId);
        return Result.success(result);
    }

    @PostMapping("/dish")
    public Result<?> addDish(@RequestBody Dish dish) {
        boolean success = dishService.addDish(dish);
        return success ? Result.success("添加成功") : Result.error("添加失败");
    }

    @PutMapping("/dish")
    public Result<?> updateDish(@RequestBody Dish dish) {
        boolean success = dishService.updateDish(dish);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/dish/{dishId}")
    public Result<?> deleteDish(@PathVariable Long dishId) {
        boolean success = dishService.deleteDish(dishId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/dishes/recommend/{userId}")
    public Result<?> getRecommendedDishesByRestrictions(@PathVariable Long userId) {
        // 获取用户的忌口列表
        List<UserRestrictions> restrictions = userRestrictionsService.getUserRestrictions(userId);
        System.out.println("用户ID: " + userId + ", 忌口数量: " + restrictions.size());
        
        // 提取所有忌口关键词（从 restriction_desc 中分割）
        List<String> restrictionKeywords = new ArrayList<>();
        for (UserRestrictions restriction : restrictions) {
            String desc = restriction.getRestrictionDesc();
            System.out.println("忌口描述: " + desc);
            if (desc != null && !desc.isEmpty()) {
                // 支持中文逗号、英文逗号、顿号分隔
                String[] keywords = desc.split("[，,、]");
                for (String keyword : keywords) {
                    String trimmed = keyword.trim();
                    if (!trimmed.isEmpty()) {
                        restrictionKeywords.add(trimmed);
                        System.out.println("添加忌口关键词: " + trimmed);
                    }
                }
            }
        }
        
        // 获取所有菜品（使用 baseMapper 直接查询 dishes 表）
        List<Dish> rawDishes = dishService.list();
        System.out.println("原始菜品总数: " + rawDishes.size());
        
        // 打印前3个菜品的isShelf值
        for (int i = 0; i < Math.min(3, rawDishes.size()); i++) {
            Dish d = rawDishes.get(i);
            System.out.println("菜品[" + d.getName() + "] isShelf=" + d.getIsShelf() + " (类型:" + (d.getIsShelf() != null ? d.getIsShelf().getClass().getSimpleName() : "null") + ")");
        }
        
        // 获取所有上架的菜品
        List<Dish> allDishes = rawDishes.stream()
                .filter(dish -> dish.getIsShelf() != null && dish.getIsShelf() == 1)
                .collect(Collectors.toList());
        System.out.println("上架菜品总数: " + allDishes.size());
        
        // 过滤掉包含忌口配料的菜品
        List<Dish> recommendedDishes = allDishes.stream().filter(dish -> {
            String ingredients = dish.getIngredients();
            String name = dish.getName();
            if (ingredients == null || ingredients.isEmpty()) {
                System.out.println("菜品[" + name + "]无配料信息，符合");
                return true; // 没有配料信息的默认符合
            }
            // 检查是否包含任何忌口关键词
            for (String keyword : restrictionKeywords) {
                if (ingredients.contains(keyword)) {
                    System.out.println("菜品[" + name + "]配料[" + ingredients + "]包含忌口[" + keyword + "], 排除");
                    return false; // 包含忌口，排除
                }
            }
            System.out.println("菜品[" + name + "]配料[" + ingredients + "]符合忌口要求");
            return true; // 不包含忌口，符合
        }).collect(Collectors.toList());
        
        System.out.println("符合忌口的菜品数量: " + recommendedDishes.size());
        return Result.success(recommendedDishes);
    }
}