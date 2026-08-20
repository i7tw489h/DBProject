package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User loginUser){
        User user = userService.login(loginUser.getUsername(), loginUser.getPassword());
        return Result.success(user);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User registerUser){
        User user = userService.register(
                registerUser.getUsername(),
                registerUser.getPassword(),
                registerUser.getName(),
                registerUser.getCollege(),
                registerUser.getPhone()
        );
        return Result.success(user);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{userId}")
    public Result<User> getUserInfo(@PathVariable Long userId){
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    /**
     * 修改密码（这里暂时先用@RequestParam，避免新增DTO）
     */
    @PostMapping("/updatePwd")
    public Result updatePassword(@RequestParam Long userId,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword){
        userService.updatePassword(userId,oldPassword,newPassword);
        return Result.success();
    }
}