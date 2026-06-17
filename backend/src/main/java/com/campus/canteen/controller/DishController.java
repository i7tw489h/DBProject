package com.campus.canteen.controller;

import com.campus.canteen.common.PageResult;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Category;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Window;
import com.campus.canteen.service.CategoryService;
import com.campus.canteen.service.DishService;
import com.campus.canteen.service.WindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            @RequestParam(defaultValue = "12") Integer pageSize) {
        // 清理空字符串
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        
        // 统一使用分页接口，支持组合筛选
        PageResult<Dish> page = dishService.getDishesPageWithFilter(categoryId, windowId, keyword, floor, pageNum, pageSize);
        return Result.success(page);
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
}