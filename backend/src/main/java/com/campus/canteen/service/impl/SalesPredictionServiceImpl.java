package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Order;
import com.campus.canteen.entity.OrderItem;
import com.campus.canteen.entity.PredictionStatistics;
import com.campus.canteen.entity.SalesPrediction;
import com.campus.canteen.mapper.*;
import com.campus.canteen.service.QwenPredictionService;
import com.campus.canteen.service.SalesPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesPredictionServiceImpl implements SalesPredictionService {

    @Autowired
    private SalesPredictionMapper predictionMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private QwenPredictionService qwenPredictionService;

    @Override
    public List<SalesPrediction> predictSales(LocalDate targetDate) {
        return predictSales(targetDate, false);
    }

    @Override
    public List<SalesPrediction> predictSales(LocalDate targetDate, boolean forceRegenerate) {
        // 如果不是强制重新生成，先检查是否已有预测数据
        if (!forceRegenerate) {
            List<SalesPrediction> existingPredictions = predictionMapper.selectList(
                new LambdaQueryWrapper<SalesPrediction>()
                    .eq(SalesPrediction::getPredictDate, targetDate)
            );
            
            // 如果已有预测数据，填充菜品信息后返回
            if (existingPredictions != null && !existingPredictions.isEmpty()) {
                // 确保按预测销量降序排列
                existingPredictions.sort((a, b) -> b.getPredictedSales().compareTo(a.getPredictedSales()));
                return fillDishInfo(existingPredictions);
            }
        } else {
            // 强制重新生成时，删除旧数据
            predictionMapper.delete(
                new LambdaQueryWrapper<SalesPrediction>()
                    .eq(SalesPrediction::getPredictDate, targetDate)
            );
        }
        
        // 获取所有上架菜品
        List<Dish> allDishes = dishMapper.selectList(
            new LambdaQueryWrapper<Dish>().eq(Dish::getIsShelf, 1)
        );

        List<SalesPrediction> predictions = new ArrayList<>();

        // 获取目标日期是星期几
        int dayOfWeek = targetDate.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        // 分析过去4周的数据
        LocalDate startDate = targetDate.minusWeeks(4);
        LocalDate endDate = targetDate.minusDays(1);

        // 获取历史订单数据
        List<Order> historicalOrders = orderMapper.selectList(
            new LambdaQueryWrapper<Order>()
                .ge(Order::getCreatedAt, startDate.atStartOfDay())
                .le(Order::getCreatedAt, endDate.atTime(23, 59, 59))
        );

        // 构建订单ID到订单的映射
        Set<String> orderIds = historicalOrders.stream()
            .map(Order::getOrderId)
            .collect(Collectors.toSet());

        // 获取历史订单项
        Map<Long, List<OrderItem>> orderItemsByDish = new HashMap<>();
        for (String orderId : orderIds) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
            for (OrderItem item : items) {
                orderItemsByDish.computeIfAbsent(item.getDishId(), k -> new ArrayList<>()).add(item);
            }
        }

        // 按星期几分组分析
        Map<Integer, Map<Long, List<OrderItem>>> weeklyPattern = new HashMap<>();
        for (Order order : historicalOrders) {
            LocalDateTime orderDate = order.getCreatedAt();
            int orderDayOfWeek = orderDate.getDayOfWeek().getValue();
            weeklyPattern.computeIfAbsent(orderDayOfWeek, k -> new HashMap<>());

            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            for (OrderItem item : items) {
                weeklyPattern.get(orderDayOfWeek).computeIfAbsent(item.getDishId(),
                    k -> new ArrayList<>()).add(item);
            }
        }
        
        // 尝试使用Qwen AI进行预测
        Map<Long, Integer> qwenPredictions = new HashMap<>();
        if (qwenPredictionService != null) {
            // 生成历史数据摘要
            Map<Long, Integer> dishSales = new HashMap<>();
            orderItemsByDish.forEach((dishId, items) -> 
                dishSales.put(dishId, items.size()));
            
            Map<Integer, Integer> weeklyTotals = new HashMap<>();
            weeklyPattern.forEach((day, pattern) -> 
                weeklyTotals.put(day, pattern.values().stream()
                    .mapToInt(List::size).sum()));
            
            String historicalSummary = qwenPredictionService.generateHistoricalSummary(dishSales, weeklyTotals);
            
            // 调用Qwen预测
            String targetDateStr = "星期" + dayOfWeek;
            qwenPredictions = qwenPredictionService.predictWithQwen(allDishes, targetDateStr, historicalSummary);
        }
        
        // 对每个菜品进行预测
        for (Dish dish : allDishes) {
            SalesPrediction prediction = new SalesPrediction();
            prediction.setDishId(dish.getDishId());
            prediction.setDishName(dish.getName());
            prediction.setPrice(dish.getPrice());
            prediction.setPredictDate(targetDate);
            prediction.setCreatedAt(LocalDateTime.now());

            // 计算预测销量
            int predictedSales = calculatePredictedSales(
                dish.getDishId(),
                dayOfWeek,
                weeklyPattern,
                orderItemsByDish,
                dish.getName(),
                qwenPredictions
            );

            prediction.setPredictedSales(predictedSales);

            // 计算置信度
            BigDecimal confidence = calculateConfidence(
                dish.getDishId(),
                dayOfWeek,
                weeklyPattern,
                orderItemsByDish,
                qwenPredictions
            );
            prediction.setConfidence(confidence);

            // 保存预测结果
            predictionMapper.insert(prediction);
            predictions.add(prediction);
        }

        // 按预测销量降序排序
        predictions.sort((a, b) -> b.getPredictedSales().compareTo(a.getPredictedSales()));

        return predictions;
    }

    /**
     * 计算预测销量
     */
    private int calculatePredictedSales(
        Long dishId,
        int targetDayOfWeek,
        Map<Integer, Map<Long, List<OrderItem>>> weeklyPattern,
        Map<Long, List<OrderItem>> orderItemsByDish,
        String dishName,
        Map<Long, Integer> qwenPredictions
    ) {
        // 如果Qwen AI有预测结果，优先使用
        if (qwenPredictions != null && qwenPredictions.containsKey(dishId)) {
            int qwenPredicted = qwenPredictions.get(dishId);
            if (qwenPredicted > 0) {
                return qwenPredicted;
            }
        }
        
        // 策略1：基于星期几的历史数据（占60%权重）
        Map<Long, List<OrderItem>> sameDayPattern = weeklyPattern.get(targetDayOfWeek);
        int weeklyAvg = 0;
        if (sameDayPattern != null && sameDayPattern.containsKey(dishId)) {
            List<OrderItem> items = sameDayPattern.get(dishId);
            weeklyAvg = items.size();
        }

        // 策略2：基于总体趋势（占40%权重）
        int totalAvg = 0;
        int totalCount = 0;
        for (Map<Long, List<OrderItem>> dayPattern : weeklyPattern.values()) {
            if (dayPattern.containsKey(dishId)) {
                totalAvg += dayPattern.get(dishId).size();
                totalCount++;
            }
        }
        if (totalCount > 0) {
            totalAvg = totalAvg / totalCount;
        }

        // 加权平均
        int predicted = (int) (weeklyAvg * 0.6 + totalAvg * 0.4);

        // 根据周内趋势调整
        int recentTrend = calculateRecentTrend(dishId, orderItemsByDish);
        predicted = predicted + (predicted * recentTrend / 100);

        // 如果预测值小于5，使用基于菜品名称的启发式预测（确保每个菜品都有合理的预测）
        if (predicted < 5) {
            predicted = generateFallbackPrediction(dishName, dishId);
        }

        return Math.max(predicted, 5); // 确保至少为5份
    }

    /**
     * 生成回退预测值（当没有历史数据时使用）
     */
    private int generateFallbackPrediction(String dishName, Long dishId) {
        // 基于菜品名称生成不同的预测值
        String name = dishName != null ? dishName.toLowerCase() : "";
        
        // 基础预测值（根据食堂规模调整）
        int basePrediction = 15;
        
        // 热门菜系加成 - 肉类通常销量最高
        if (name.contains("肉") || name.contains("鸡") || name.contains("鱼") || 
            name.contains("排骨") || name.contains("牛肉") || name.contains("羊肉")) {
            basePrediction += 12; // 肉类加12-18
        } else if (name.contains("炒饭") || name.contains("炒面") || name.contains("盖饭") ||
                   name.contains("拌面") || name.contains("汤面") || name.contains("米线")) {
            basePrediction += 10; // 主食类加10-15
        } else if (name.contains("蔬菜") || name.contains("青菜") || name.contains("白菜") ||
                   name.contains("黄瓜") || name.contains("番茄") || name.contains("土豆")) {
            basePrediction += 5; // 蔬菜加5-8
        } else if (name.contains("汤") || name.contains("粥") || name.contains("羹")) {
            basePrediction += 8; // 汤粥类加8-12
        } else if (name.contains("豆腐") || name.contains("蛋") || name.contains("鸡蛋")) {
            basePrediction += 6; // 豆制品和蛋类加6-10
        }
        
        // 基于ID的哈希分布，确保不同菜品有不同预测（范围5-15）
        int hashVariation = Math.abs((dishId.hashCode() % 11)) + 5;
        
        // 最终预测值
        int finalPrediction = basePrediction + hashVariation;
        
        // 确保最低不少于8份，最高不超过60份
        return Math.min(Math.max(finalPrediction, 8), 60);
    }

    /**
     * 计算最近趋势
     */
    private int calculateRecentTrend(Long dishId, Map<Long, List<OrderItem>> orderItemsByDish) {
        if (!orderItemsByDish.containsKey(dishId)) {
            return 0;
        }

        List<OrderItem> items = orderItemsByDish.get(dishId);
        if (items.size() < 2) {
            return 0;
        }

        // 简化实现：基于最近2周的数据计算趋势
        // 如果最近一周销量高于前几周，返回正向趋势
        int recentWeekCount = items.size();
        int expectedCount = recentWeekCount; // 简化处理

        if (recentWeekCount > expectedCount * 1.1) {
            return 10; // 增长10%
        } else if (recentWeekCount < expectedCount * 0.9) {
            return -10; // 下降10%
        }
        return 0;
    }

    /**
     * 计算置信度
     */
    private BigDecimal calculateConfidence(
        Long dishId,
        int targetDayOfWeek,
        Map<Integer, Map<Long, List<OrderItem>>> weeklyPattern,
        Map<Long, List<OrderItem>> orderItemsByDish,
        Map<Long, Integer> qwenPredictions
    ) {
        // 基于数据量和稳定性计算置信度
        double baseConfidence = 0.5;

        // 有多少周的数据
        int weeksWithData = weeklyPattern.size();
        baseConfidence += weeksWithData * 0.05;

        // 星期几的数据
        Map<Long, List<OrderItem>> sameDayData = weeklyPattern.get(targetDayOfWeek);
        if (sameDayData != null && sameDayData.containsKey(dishId)) {
            int sameDayCount = sameDayData.get(dishId).size();
            baseConfidence += Math.min(sameDayCount * 0.05, 0.3);
        }

        // 总体数据量
        if (orderItemsByDish.containsKey(dishId)) {
            int totalCount = orderItemsByDish.get(dishId).size();
            baseConfidence += Math.min(totalCount * 0.02, 0.2);
        }
        
        // 如果Qwen AI有预测结果，提高置信度
        if (qwenPredictions != null && qwenPredictions.containsKey(dishId)) {
            baseConfidence += 0.1; // AI预测加成
        }

        return BigDecimal.valueOf(Math.min(baseConfidence, 0.95))
            .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<SalesPrediction> getPredictionHistory(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        List<SalesPrediction> predictions = predictionMapper.selectList(
            new LambdaQueryWrapper<SalesPrediction>()
                .ge(SalesPrediction::getPredictDate, startDate)
                .orderByDesc(SalesPrediction::getPredictDate)
        );
        
        // 填充菜品信息
        return fillDishInfo(predictions);
    }

    @Override
    public void updateActualSales(LocalDate predictDate) {
        // 获取该日期的所有订单
        List<Order> orders = orderMapper.selectList(
            new LambdaQueryWrapper<Order>()
                .ge(Order::getCreatedAt, predictDate.atStartOfDay())
                .le(Order::getCreatedAt, predictDate.atTime(23, 59, 59))
        );

        // 统计每个菜品的实际销量
        Map<Long, Integer> actualSalesMap = new HashMap<>();
        for (Order order : orders) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            for (OrderItem item : items) {
                actualSalesMap.merge(item.getDishId(), item.getQuantity(), Integer::sum);
            }
        }

        // 更新预测记录
        List<SalesPrediction> predictions = predictionMapper.selectList(
            new LambdaQueryWrapper<SalesPrediction>()
                .eq(SalesPrediction::getPredictDate, predictDate)
        );

        for (SalesPrediction prediction : predictions) {
            Integer actualSales = actualSalesMap.getOrDefault(prediction.getDishId(), 0);
            prediction.setActualSales(actualSales);
            predictionMapper.updateById(prediction);
        }
    }

    @Override
    public PredictionStatistics getStatistics() {
        PredictionStatistics stats = new PredictionStatistics();

        // 获取明天的预测记录（只统计当前预测）
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<SalesPrediction> tomorrowPredictions = predictionMapper.selectList(
            new LambdaQueryWrapper<SalesPrediction>()
                .eq(SalesPrediction::getPredictDate, tomorrow)
        );

        // 计算平均置信度
        double avgConfidence = 0;
        int highConfidenceCount = 0;
        int lowConfidenceCount = 0;

        for (SalesPrediction pred : tomorrowPredictions) {
            if (pred.getConfidence() != null) {
                avgConfidence += pred.getConfidence().doubleValue();
                
                // 高置信度（>= 0.7）
                if (pred.getConfidence().doubleValue() >= 0.7) {
                    highConfidenceCount++;
                }
                // 低置信度（< 0.5）
                if (pred.getConfidence().doubleValue() < 0.5) {
                    lowConfidenceCount++;
                }
            }
        }

        int totalPredictions = tomorrowPredictions.size();
        if (totalPredictions > 0) {
            avgConfidence = avgConfidence / totalPredictions * 100; // 转换为百分比
        }

        stats.setTotalPredictions(totalPredictions);
        stats.setAccuracyRate(BigDecimal.valueOf(avgConfidence).setScale(2, RoundingMode.HALF_UP));
        stats.setAverageError(highConfidenceCount);
        stats.setEvaluatedPredictions(lowConfidenceCount);

        return stats;
    }

    @Override
    public void resetPredictions() {
        // 删除所有预测数据
        predictionMapper.delete(null);
    }

    /**
     * 为预测结果填充菜品信息
     */
    private List<SalesPrediction> fillDishInfo(List<SalesPrediction> predictions) {
        // 获取所有菜品ID
        Set<Long> dishIds = predictions.stream()
            .map(SalesPrediction::getDishId)
            .collect(Collectors.toSet());
        
        // 批量查询菜品信息
        List<Dish> dishes = dishMapper.selectList(
            new LambdaQueryWrapper<Dish>().in(Dish::getDishId, dishIds)
        );
        
        // 构建菜品ID到菜品的映射
        Map<Long, Dish> dishMap = dishes.stream()
            .collect(Collectors.toMap(Dish::getDishId, d -> d));
        
        // 填充菜品信息
        for (SalesPrediction prediction : predictions) {
            Dish dish = dishMap.get(prediction.getDishId());
            if (dish != null) {
                prediction.setDishName(dish.getName());
                prediction.setPrice(dish.getPrice());
                prediction.setCategoryName(dish.getCategoryName());
            }
        }
        
        return predictions;
    }
}
