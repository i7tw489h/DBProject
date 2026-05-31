package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.HealthEvaluation;
import com.campus.canteen.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nutrition")
@CrossOrigin
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping("/today/{userId}")
    public Result getTodayNutrition(@PathVariable Long userId) {
        Map<String, BigDecimal> nutrition = nutritionService.getTodayNutrition(userId);
        return Result.success(nutrition);
    }

    @GetMapping("/history/{userId}")
    public Result getHistoryNutrition(@PathVariable Long userId, @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> history = nutritionService.getHistoryNutrition(userId, days);
        return Result.success(history);
    }

    @GetMapping("/targets/{userId}")
    public Result getNutritionTargets(@PathVariable Long userId) {
        Map<String, BigDecimal> targets = nutritionService.getNutritionTargets(userId);
        return Result.success(targets);
    }

    @GetMapping("/evaluation/{userId}")
    public Result getHealthEvaluation(@PathVariable Long userId) {
        HealthEvaluation evaluation = nutritionService.evaluateHealth(userId);
        return Result.success(evaluation);
    }
}
