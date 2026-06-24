package com.campus.canteen.entity;

import java.math.BigDecimal;

public class PredictionStatistics {
    private BigDecimal accuracyRate;
    private Integer averageError;
    private Integer totalPredictions;
    private Integer evaluatedPredictions;

    public PredictionStatistics() {}

    public BigDecimal getAccuracyRate() { return accuracyRate; }
    public void setAccuracyRate(BigDecimal accuracyRate) { this.accuracyRate = accuracyRate; }
    public Integer getAverageError() { return averageError; }
    public void setAverageError(Integer averageError) { this.averageError = averageError; }
    public Integer getTotalPredictions() { return totalPredictions; }
    public void setTotalPredictions(Integer totalPredictions) { this.totalPredictions = totalPredictions; }
    public Integer getEvaluatedPredictions() { return evaluatedPredictions; }
    public void setEvaluatedPredictions(Integer evaluatedPredictions) { this.evaluatedPredictions = evaluatedPredictions; }
}
