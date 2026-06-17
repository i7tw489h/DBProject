package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.PredictionStatistics;
import com.campus.canteen.entity.SalesPrediction;
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

    /**
     * 预测指定日期的菜品销量
     */
    @GetMapping("/predict")
    public Result<?> predictSales(
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate targetDate) {
        
        if (targetDate == null) {
            // 默认预测明天的销量
            targetDate = LocalDate.now().plusDays(1);
        }

        try {
            List<SalesPrediction> predictions = predictionService.predictSales(targetDate);
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
     * 生成备餐建议
     */
    @GetMapping("/suggestions")
    public Result<?> getPreparationSuggestions() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<SalesPrediction> predictions = predictionService.predictSales(tomorrow);

            // 生成备餐建议
            List<PreparationSuggestion> suggestions = new java.util.ArrayList<>();
            for (SalesPrediction pred : predictions) {
                if (pred.getPredictedSales() > 0) {
                    PreparationSuggestion suggestion = new PreparationSuggestion();
                    suggestion.setDishId(pred.getDishId());
                    suggestion.setDishName(pred.getDishName());
                    suggestion.setRecommendedQuantity(pred.getPredictedSales() + 5); // 额外备5份
                    suggestion.setMinQuantity(pred.getPredictedSales());
                    suggestion.setMaxQuantity(pred.getPredictedSales() + 10);
                    suggestion.setConfidence(pred.getConfidence());
                    suggestion.setPriority(pred.getPredictedSales() >= 20 ? "high" : 
                                         pred.getPredictedSales() >= 10 ? "medium" : "low");
                    suggestions.add(suggestion);
                }
            }

            return Result.success(suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("生成建议失败: " + e.getMessage());
        }
    }

    /**
     * 备餐建议内部类
     */
    public static class PreparationSuggestion {
        private Long dishId;
        private String dishName;
        private Integer recommendedQuantity;
        private Integer minQuantity;
        private Integer maxQuantity;
        private java.math.BigDecimal confidence;
        private String priority;

        public Long getDishId() { return dishId; }
        public void setDishId(Long dishId) { this.dishId = dishId; }
        public String getDishName() { return dishName; }
        public void setDishName(String dishName) { this.dishName = dishName; }
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
