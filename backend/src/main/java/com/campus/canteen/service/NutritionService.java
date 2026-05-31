package com.campus.canteen.service;

import com.campus.canteen.entity.HealthEvaluation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface NutritionService {
    Map<String, BigDecimal> getTodayNutrition(Long userId);
    List<Map<String, Object>> getHistoryNutrition(Long userId, int days);
    Map<String, BigDecimal> getNutritionTargets(Long userId);
    HealthEvaluation evaluateHealth(Long userId);
}

