package com.campus.canteen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.UserRestrictions;
import com.campus.canteen.mapper.UserRestrictionsMapper;
import com.campus.canteen.service.AIRecommendService;
import com.campus.canteen.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIRecommendController {

    @Autowired
    private AIRecommendService aiRecommendService;

    @Autowired
    private UserRestrictionsMapper userRestrictionsMapper;

    @Autowired
    private AIService aiService;

    @GetMapping("/test")
    public Result<?> testAiConnection() {
        return Result.success(aiService.testConnection());
    }

    @GetMapping("/recommend/{userId}")
    public Result<?> getRecommendations(@PathVariable Long userId) {
        List<Map<String, Object>> recommendations = aiRecommendService.getCombinedRecommendations(userId);
        return Result.success(recommendations);
    }

    @GetMapping("/meal/{userId}")
    public Result<?> intelligentMeal(@PathVariable Long userId, @RequestParam String type) {
        List<Dish> dishes = aiRecommendService.intelligentMeal(userId, type);
        return Result.success(dishes);
    }

    @GetMapping("/history/{userId}")
    public Result<?> recommendByHistory(@PathVariable Long userId) {
        List<Dish> dishes = aiRecommendService.recommendByHistory(userId, 6);
        return Result.success(dishes);
    }

    @GetMapping("/goal/{userId}")
    public Result<?> recommendByGoal(@PathVariable Long userId, @RequestParam(defaultValue = "0") int goal) {
        List<Dish> dishes = aiRecommendService.recommendByGoal(userId, goal, 6);
        return Result.success(dishes);
    }

    @GetMapping("/restrictions/{userId}")
    public Result<?> getUserRestrictions(@PathVariable Long userId) {
        LambdaQueryWrapper<UserRestrictions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRestrictions::getUserId, userId);
        List<UserRestrictions> restrictions = userRestrictionsMapper.selectList(wrapper);
        return Result.success(restrictions);
    }

    @PostMapping("/restrictions")
    public Result<?> addRestriction(@RequestBody UserRestrictions restriction) {
        LambdaQueryWrapper<UserRestrictions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRestrictions::getUserId, restriction.getUserId())
               .eq(UserRestrictions::getRestrictionType, restriction.getRestrictionType());
        UserRestrictions existing = userRestrictionsMapper.selectOne(wrapper);
        if (existing != null) {
            return Result.error("该忌口已设置");
        }
        userRestrictionsMapper.insert(restriction);
        return Result.success("忌口设置成功");
    }

    @DeleteMapping("/restrictions/{restrictionId}")
    public Result<?> deleteRestriction(@PathVariable Long restrictionId) {
        userRestrictionsMapper.deleteById(restrictionId);
        return Result.success("忌口删除成功");
    }
}
