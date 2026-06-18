package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.PredictionStatistics;
import com.campus.canteen.entity.SalesPrediction;
import com.campus.canteen.service.QwenPredictionService;
import com.campus.canteen.service.SalesPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/prediction")
@CrossOrigin
public class SalesPredictionController {

    @Autowired
    private SalesPredictionService predictionService;

    @Autowired
    private QwenPredictionService qwenPredictionService;

    /**
     * 预测指定日期的菜品销量
     */
    @GetMapping("/predict")
    public Result<?> predictSales(
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate targetDate,
            @RequestParam(required = false) String forceRegenerate) {
        
        if (targetDate == null) {
            // 默认预测明天的销量
            targetDate = LocalDate.now().plusDays(1);
        }

        // 解析布尔参数（处理字符串形式）
        boolean force = Boolean.parseBoolean(forceRegenerate);

        try {
            List<SalesPrediction> predictions = predictionService.predictSales(targetDate, force);
            return Result.success(predictions);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("预测失败: " + e.getMessage());
        }
    }

    /**
     * 获取历史预测记录
     */
    @GetMapping("/history")
    public Result<?> getPredictionHistory(@RequestParam(defaultValue = "7") Integer days) {
        try {
            List<SalesPrediction> history = predictionService.getPredictionHistory(days);
            return Result.success(history);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取历史记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新实际销量
     */
    @PostMapping("/update-actual")
    public Result<?> updateActualSales(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate predictDate) {
        try {
            predictionService.updateActualSales(predictDate);
            return Result.success("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取预测统计数据
     */
    @GetMapping("/statistics")
    public Result<?> getStatistics() {
        try {
            PredictionStatistics stats = predictionService.getStatistics();
            return Result.success(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取明天预测结果（快捷接口）
     */
    @GetMapping("/tomorrow")
    public Result<?> getTomorrowPrediction() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<SalesPrediction> predictions = predictionService.predictSales(tomorrow);
            return Result.success(predictions);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("预测失败: " + e.getMessage());
        }
    }

    /**
     * 重置预测数据（重新生成）
     */
    @DeleteMapping("/reset")
    public Result<?> resetPredictions() {
        try {
            predictionService.resetPredictions();
            return Result.success("预测数据已重置");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("重置失败: " + e.getMessage());
        }
    }
    /**
     * 生成备餐建议（优先使用Qwen AI）
     */
    @GetMapping("/suggestions")
    public Result<?> getPreparationSuggestions() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<SalesPrediction> predictions = predictionService.predictSales(tomorrow);

            // 生成备餐建议（优先使用Qwen AI，失败则使用规则方法）
            List<PreparationSuggestion> suggestions = new java.util.ArrayList<>();
            
            // 尝试使用Qwen AI生成备餐建议
            java.util.Map<Long, QwenPredictionService.PreparationAdvice> aiSuggestions = 
                qwenPredictionService.generateSuggestionsWithQwen(predictions);
            
            // 计算所有菜品的总预测销量，用于归一化
            int totalPredicted = predictions.stream()
                .mapToInt(SalesPrediction::getPredictedSales)
                .sum();
            
            for (int i = 0; i < predictions.size(); i++) {
                SalesPrediction pred = predictions.get(i);
                PreparationSuggestion suggestion = new PreparationSuggestion();
                suggestion.setDishId(pred.getDishId());
                suggestion.setDishName(pred.getDishName());
                suggestion.setPrice(pred.getPrice()); // 设置价格
                
                // 检查是否有AI生成的建议
                QwenPredictionService.PreparationAdvice aiAdvice = aiSuggestions.get(pred.getDishId());
                
                if (aiAdvice != null) {
                    // 使用AI生成的建议
                    suggestion.setMinQuantity(aiAdvice.getMinQuantity());
                    suggestion.setRecommendedQuantity(aiAdvice.getRecommendedQuantity());
                    suggestion.setMaxQuantity(aiAdvice.getMaxQuantity());
                    suggestion.setPriority(aiAdvice.getPriority());
                } else {
                    // 使用规则方法生成建议
                    int predicted = pred.getPredictedSales();
                    double confidence = pred.getConfidence() != null ? pred.getConfidence().doubleValue() : 0.5;
                    
                    // 基础数量：预测销量（如果预测为0，则使用最低保障10份）
                    int baseQuantity = Math.max(predicted, 10);
                    
                    // 根据预测销量占比调整
                    double popularityRatio = totalPredicted > 0 ? (double) predicted / totalPredicted : 0.1;
                    
                    // 最小备餐量：预测销量的90%（最低保障）
                    int minQuantity = (int)(baseQuantity * 0.9);
                    
                    // 建议备餐量：预测销量 + 保险量（置信度越低，保险量越高）
                    int buffer = (int)(baseQuantity * 0.2 * (1 - confidence));
                    int recommended = baseQuantity + buffer;
                    
                    // 最大备餐量：预测销量的120%（高销量菜品可以多备）
                    int maxQuantity = (int)(baseQuantity * 1.2);
                    
                    // 确保最小值小于建议值，建议值小于最大值
                    minQuantity = Math.max(8, minQuantity);
                    recommended = Math.max(minQuantity + 2, recommended);
                    maxQuantity = Math.max(recommended + 5, maxQuantity);
                    
                    suggestion.setMinQuantity(minQuantity);
                    suggestion.setRecommendedQuantity(recommended);
                    suggestion.setMaxQuantity(maxQuantity);
                    
                    // 优先级根据预测销量划分（高销量=高优先级）
                    if (predicted >= 25) {
                        suggestion.setPriority("high");
                    } else if (predicted >= 15) {
                        suggestion.setPriority("medium");
                    } else {
                        suggestion.setPriority("low");
                    }
                }
                
                suggestion.setConfidence(pred.getConfidence());
                suggestions.add(suggestion);
            }
            
            // 按优先级和预测销量排序（高优先级在前，同优先级内按销量降序）
            suggestions.sort((a, b) -> {
                int priorityCompare = getPriorityValue(b.getPriority()) - getPriorityValue(a.getPriority());
                if (priorityCompare != 0) return priorityCompare;
                
                // 同优先级按预测销量降序
                Integer aPredicted = predictions.stream()
                    .filter(p -> p.getDishId().equals(a.getDishId()))
                    .map(SalesPrediction::getPredictedSales)
                    .findFirst().orElse(0);
                Integer bPredicted = predictions.stream()
                    .filter(p -> p.getDishId().equals(b.getDishId()))
                    .map(SalesPrediction::getPredictedSales)
                    .findFirst().orElse(0);
                return bPredicted.compareTo(aPredicted);
            });

            return Result.success(suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("生成建议失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取优先级数值（用于排序）
     */
    private int getPriorityValue(String priority) {
        if ("high".equals(priority)) return 3;
        if ("medium".equals(priority)) return 2;
        if ("low".equals(priority)) return 1;
        return 0;
    }

    /**
     * 备餐建议内部类
     */
    public static class PreparationSuggestion {
        private Long dishId;
        private String dishName;
        private java.math.BigDecimal price;
        private Integer recommendedQuantity;
        private Integer minQuantity;
        private Integer maxQuantity;
        private java.math.BigDecimal confidence;
        private String priority;

        public Long getDishId() { return dishId; }
        public void setDishId(Long dishId) { this.dishId = dishId; }
        public String getDishName() { return dishName; }
        public void setDishName(String dishName) { this.dishName = dishName; }
        public java.math.BigDecimal getPrice() { return price; }
        public void setPrice(java.math.BigDecimal price) { this.price = price; }
        public Integer getRecommendedQuantity() { return recommendedQuantity; }
        public void setRecommendedQuantity(Integer recommendedQuantity) { this.recommendedQuantity = recommendedQuantity; }
        public Integer getMinQuantity() { return minQuantity; }
        public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }
        public Integer getMaxQuantity() { return maxQuantity; }
        public void setMaxQuantity(Integer maxQuantity) { this.maxQuantity = maxQuantity; }
        public java.math.BigDecimal getConfidence() { return confidence; }
        public void setConfidence(java.math.BigDecimal confidence) { this.confidence = confidence; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }
}
