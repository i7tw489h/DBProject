package com.campus.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.campus.canteen.entity.UserHealthProfile;
import com.campus.canteen.service.UserHealthProfileService;
import com.campus.canteen.common.Result;

@RestController
@RequestMapping("/healthProfile")
public class HealthProfileController {

    @Autowired
    private UserHealthProfileService profileService;

    //保存/更新健康档案（首次初始化、修改档案）
    @PostMapping("/save")
    public Result saveProfile(@RequestBody UserHealthProfile profile){
        profileService.saveOrUpdate(profile);
        return Result.success();
    }

    //根据用户id获取档案
    @GetMapping("/get/{userId}")
    public Result<UserHealthProfile> getByUserId(@PathVariable Long userId){
        UserHealthProfile p = profileService.getByUserId(userId);
        return Result.success(p);
    }
}
