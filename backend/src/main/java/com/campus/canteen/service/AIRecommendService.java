package com.campus.canteen.service;

import com.campus.canteen.entity.Dish;

import java.util.List;
import java.util.Map;

public interface AIRecommendService {
    List<Dish> recommendByHistory(Long userId, int limit); // 根据用户历史推荐
    List<Dish> recommendByGoal(Long userId, int dietGoal, int limit); // 根据用户目标推荐
    List<Dish> recommendByRestrictions(Long userId, int limit); // 根据用户限制推荐
    List<Map<String, Object>> getCombinedRecommendations(Long userId);// 获取AI为您推荐
    List<Dish> intelligentMeal(Long userId, String mealType); // 智能餐品推荐
}
