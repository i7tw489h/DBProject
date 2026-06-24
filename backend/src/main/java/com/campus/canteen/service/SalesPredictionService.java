package com.campus.canteen.service;

import com.campus.canteen.entity.PredictionStatistics;
import com.campus.canteen.entity.SalesPrediction;

import java.time.LocalDate;
import java.util.List;

public interface SalesPredictionService {

    /**
     * 预测指定日期的菜品销量
     * @param targetDate 目标日期
     * @return 预测结果列表
     */
    List<SalesPrediction> predictSales(LocalDate targetDate);
    
    /**
     * 预测销量（支持强制重新生成）
     * @param targetDate 目标日期
     * @param forceRegenerate 是否强制重新生成
     * @return 预测结果列表
     */
    List<SalesPrediction> predictSales(LocalDate targetDate, boolean forceRegenerate);
    
    /**
     * 获取历史预测记录
     * @param days 历史天数
     * @return 历史预测记录列表
     */
    List<SalesPrediction> getPredictionHistory(int days);

    /**
     * 更新实际销量（用于对比预测准确性）
     * @param predictDate 预测日期
     */
    void updateActualSales(LocalDate predictDate);

    /**
     * 获取预测统计数据
     * @return 统计数据
     */
    PredictionStatistics getStatistics();

    /**
     * 重置所有预测数据
     */
    void resetPredictions();
}
