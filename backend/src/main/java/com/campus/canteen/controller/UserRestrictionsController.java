package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.UserRestrictions;
import com.campus.canteen.service.UserRestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserRestrictionsController {

    @Autowired
    private UserRestrictionsService userRestrictionsService;

    @GetMapping("/user/restrictions/{userId}")
    public Result<?> getUserRestrictions(@PathVariable Long userId) {
        List<UserRestrictions> restrictions = userRestrictionsService.getUserRestrictions(userId);
        return Result.success(restrictions);
    }

    @PostMapping("/user/restriction")
    public Result<?> saveUserRestriction(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        Integer restrictionType = Integer.valueOf(body.get("restrictionType").toString());
        String restrictionDesc = (String) body.get("restrictionDesc");
        userRestrictionsService.saveUserRestriction(userId, restrictionType, restrictionDesc);
        return Result.success("保存成功");
    }

    @DeleteMapping("/user/restriction/{restrictionId}")
    public Result<?> deleteUserRestriction(@PathVariable Long restrictionId) {
        userRestrictionsService.deleteUserRestriction(restrictionId);
        return Result.success("删除成功");
    }
}
