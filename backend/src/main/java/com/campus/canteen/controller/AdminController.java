package com.campus.canteen.controller;

import com.campus.canteen.common.PageResult;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Nutrition;
import com.campus.canteen.entity.Order;
import com.campus.canteen.mapper.DishMapper;
import com.campus.canteen.mapper.NutritionMapper;
import com.campus.canteen.mapper.OrderMapper;
import com.campus.canteen.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private NutritionMapper nutritionMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private javax.sql.DataSource dataSource;

    @GetMapping("/dishes")
    public Result<?> getDishList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long windowId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        
        Page<Dish> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(Dish::getName, keyword);
        }
        if (categoryId != null) {
            queryWrapper.eq(Dish::getCategoryId, categoryId);
        }
        if (windowId != null) {
            queryWrapper.eq(Dish::getWindowId, windowId);
        }
        if (isActive != null) {
            queryWrapper.eq(Dish::getIsShelf, isActive ? 1 : 0);
        }
        
        if (sortField != null && !sortField.isEmpty()) {
            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
            if ("dishId".equals(sortField)) {
                queryWrapper.orderBy(true, isAsc, Dish::getDishId);
            } else if ("price".equals(sortField)) {
                queryWrapper.orderBy(true, isAsc, Dish::getPrice);
            } else if ("stock".equals(sortField)) {
                queryWrapper.orderBy(true, isAsc, Dish::getStock);
            } else if ("salesCount".equals(sortField)) {
                queryWrapper.orderBy(true, isAsc, Dish::getSalesCount);
            }
        } else {
            queryWrapper.orderByAsc(Dish::getDishId);
        }
        
        IPage<Dish> result = dishMapper.selectPage(page, queryWrapper);
        
        List<Map<String, Object>> dishList = result.getRecords().stream().map(dish -> {
            Map<String, Object> map = new HashMap<>();
            map.put("dishId", dish.getDishId());
            map.put("name", dish.getName());
            map.put("price", dish.getPrice());
            map.put("imageUrl", dish.getImageUrl());
            map.put("description", dish.getDescription());
            map.put("ingredients", dish.getIngredients());
            map.put("categoryId", dish.getCategoryId());
            map.put("categoryName", getCategoryName(dish.getCategoryId()));
            map.put("windowId", dish.getWindowId());
            map.put("windowName", getWindowName(dish.getWindowId()));
            map.put("stock", dish.getStock());
            map.put("isActive", dish.getIsShelf() != null && dish.getIsShelf() == 1);
            map.put("salesCount", dish.getSalesCount());
            return map;
        }).collect(Collectors.toList());
        
        PageResult<Map<String, Object>> pageResult = new PageResult<>(result.getTotal(), dishList, pageNum.longValue(), pageSize.longValue());
        return Result.success(pageResult);
    }
    
    private String getCategoryName(Long categoryId) {
        if (categoryId == null) return "";
        return dishMapper.getCategoryName(categoryId);
    }
    
    private String getWindowName(Long windowId) {
        if (windowId == null) return "";
        return dishMapper.getWindowName(windowId);
    }

    @PostMapping("/dishes")
    public Result<?> addDish(@RequestBody Dish dish) {
        Long maxId = dishMapper.selectMaxId();
        Long newId = (maxId == null) ? 1L : maxId + 1;
        dish.setDishId(newId);
        
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
    
    @PutMapping("/dishes/batch/status")
    public Result<?> batchToggleStatus(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        Boolean isActive = (Boolean) params.get("isActive");
        
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要操作的菜品");
        }
        
        for (Long id : ids) {
            Dish dish = dishMapper.selectById(id);
            if (dish != null) {
                dish.setIsShelf(isActive ? 1 : 0);
                dishMapper.updateById(dish);
            }
        }
        
        return Result.success("批量操作成功");
    }
    
    @DeleteMapping("/dishes/batch")
    public Result<?> batchDeleteDishes(@RequestBody Map<String, Object> params) {
        List<Long> ids = (List<Long>) params.get("ids");
        
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的菜品");
        }
        
        for (Long id : ids) {
            nutritionMapper.delete(new LambdaQueryWrapper<Nutrition>().eq(Nutrition::getDishId, id));
            dishMapper.deleteById(id);
        }
        
        return Result.success("批量删除成功");
    }

    @GetMapping("/sales-ranking")
    public Result<?> getSalesRanking() {
        List<Dish> dishes = dishMapper.selectList(null);
        // 计算销售额并按销售额排序
        List<Map<String, Object>> ranking = dishes.stream().map(dish -> {
            Map<String, Object> item = new HashMap<>();
            item.put("dishId", dish.getDishId());
            item.put("name", dish.getName());
            int salesCount = dish.getSalesCount() != null ? dish.getSalesCount() : 0;
            BigDecimal price = dish.getPrice() != null ? dish.getPrice() : BigDecimal.ZERO;
            BigDecimal salesAmount = price.multiply(new BigDecimal(salesCount));
            item.put("salesCount", salesCount);
            item.put("salesAmount", salesAmount);
            return item;
        }).sorted((a, b) -> {
            BigDecimal sa = (BigDecimal) a.get("salesAmount");
            BigDecimal sb = (BigDecimal) b.get("salesAmount");
            return sb.compareTo(sa);
        }).collect(Collectors.toList());
        
        return Result.success(ranking.subList(0, Math.min(10, ranking.size())));
    }

    @GetMapping("/sales-statistics")
    public Result<?> getSalesStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计总菜品数
        Long totalDishes = dishMapper.selectCount(null);
        stats.put("totalDishes", totalDishes);
        
        // 统计总订单数和总销售额（只统计已支付或已完成的订单）
        List<Order> allOrders = orderMapper.selectAll();
        BigDecimal totalSales = BigDecimal.ZERO;
        int totalOrders = 0;
        for (Order order : allOrders) {
            // 只统计有效订单（已支付、已接单、已出餐、待取餐、已完成）
            if (order.getStatus() != null && order.getStatus() >= 1 && order.getStatus() <= 4) {
                if (order.getTotalAmount() != null) {
                    totalSales = totalSales.add(order.getTotalAmount());
                }
                totalOrders++;
            }
        }
        stats.put("totalSales", totalSales);
        stats.put("totalOrders", totalOrders);
        
        // 统计用户总数
        Long totalUsers = userMapper.selectCount(null);
        stats.put("totalUsers", totalUsers);
        
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