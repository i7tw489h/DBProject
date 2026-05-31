package com.campus.canteen.controller;

import com.campus.canteen.common.PageResult;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Nutrition;
import com.campus.canteen.mapper.DishMapper;
import com.campus.canteen.mapper.NutritionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private NutritionMapper nutritionMapper;

    @Autowired
    private javax.sql.DataSource dataSource;

    @GetMapping("/dishes")
    public Result<?> getDishList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Dish> page = new Page<>(pageNum, pageSize);
        IPage<Dish> result = dishMapper.selectPage(page, null);
        PageResult<Dish> pageResult = new PageResult<Dish>(result.getTotal(), result.getRecords(), pageNum.longValue(), pageSize.longValue());
        return Result.success(pageResult);
    }

    @PostMapping("/dishes")
    public Result<?> addDish(@RequestBody Dish dish) {
        dishMapper.insert(dish);
        
        Nutrition nutrition = new Nutrition();
        nutrition.setDishId(dish.getDishId());
        nutritionMapper.insert(nutrition);
        
        return Result.success("添加成功");
    }

    @PutMapping("/dishes")
    public Result<?> updateDish(@RequestBody Dish dish) {
        dishMapper.updateById(dish);
        return Result.success("更新成功");
    }

    @DeleteMapping("/dishes/{dishId}")
    public Result<?> deleteDish(@PathVariable Long dishId) {
        nutritionMapper.delete(new LambdaQueryWrapper<Nutrition>().eq(Nutrition::getDishId, dishId));
        dishMapper.deleteById(dishId);
        return Result.success("删除成功");
    }

    @PutMapping("/dishes/status/{dishId}")
    public Result<?> toggleDishStatus(@PathVariable Long dishId) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish != null) {
            dish.setIsShelf(dish.getIsShelf() == 1 ? 0 : 1);
            dishMapper.updateById(dish);
            return Result.success("操作成功");
        }
        return Result.error("菜品不存在");
    }

    @GetMapping("/sales-ranking")
    public Result<?> getSalesRanking() {
        List<Dish> dishes = dishMapper.selectList(null);
        dishes.sort((a, b) -> {
            int sa = a.getSalesCount() != null ? a.getSalesCount() : 0;
            int sb = b.getSalesCount() != null ? b.getSalesCount() : 0;
            return sb - sa;
        });
        return Result.success(dishes.subList(0, Math.min(10, dishes.size())));
    }

    @GetMapping("/sales-statistics")
    public Result<?> getSalesStatistics() {
        Map<String, Object> stats = new HashMap<>();
        Long totalDishes = dishMapper.selectCount(null);
        stats.put("totalDishes", totalDishes);
        stats.put("totalSales", 0);
        stats.put("totalOrders", 0);
        stats.put("totalUsers", 0);
        return Result.success(stats);
    }

    @GetMapping("/low-stock")
    public Result<?> getLowStockAlert() {
        List<Dish> lowStock = dishMapper.selectList(
            new LambdaQueryWrapper<Dish>().lt(Dish::getStock, 20));
        return Result.success(lowStock);
    }

    @PutMapping("/nutrition")
    public Result<?> updateNutrition(@RequestBody Nutrition nutrition) {
        nutritionMapper.updateById(nutrition);
        return Result.success("更新成功");
    }

    @GetMapping("/init-db")
    public Result<?> initDatabase() {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            StringBuilder result = new StringBuilder();
            
            try {
                stmt.execute("ALTER TABLE dishes ADD COLUMN description VARCHAR(500) DEFAULT NULL AFTER image_url");
                result.append("dishes.description 添加成功; ");
            } catch (java.sql.SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                    result.append("dishes.description 已存在; ");
                } else {
                    throw e;
                }
            }
            
            try {
                stmt.execute("ALTER TABLE order_items ADD COLUMN name VARCHAR(100) DEFAULT NULL AFTER dish_id");
                result.append("order_items.name 添加成功; ");
            } catch (java.sql.SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                    result.append("order_items.name 已存在; ");
                } else {
                    throw e;
                }
            }
            
            try {
                stmt.execute("ALTER TABLE order_items ADD COLUMN image_url VARCHAR(255) DEFAULT NULL AFTER name");
                result.append("order_items.image_url 添加成功; ");
            } catch (java.sql.SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                    result.append("order_items.image_url 已存在; ");
                } else {
                    throw e;
                }
            }
            
            return Result.success(result.toString());
        } catch (Exception e) {
            return Result.error("操作失败: " + e.getMessage());
        }
    }
}