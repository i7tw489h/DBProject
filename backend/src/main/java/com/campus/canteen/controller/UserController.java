package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody(required = false) Map<String, String> body,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String account,
                              @RequestParam(required = false) String password,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String college,
                              @RequestParam(required = false) String phone) {

        String userAccount = (body != null && body.get("username") != null) ? body.get("username") :
                           (body != null && body.get("account") != null) ? body.get("account") :
                           (username != null ? username : account);

        String userPassword = (body != null && body.get("password") != null) ? body.get("password") : password;
        String userName = (body != null && body.get("name") != null) ? body.get("name") : name;
        String userCollege = (body != null && body.get("college") != null) ? body.get("college") : college;
        String userPhone = (body != null && body.get("phone") != null) ? body.get("phone") : phone;

        if (userAccount == null || userPassword == null || userName == null) {
            return Result.error("参数不完整");
        }

        User user = userService.register(userAccount, userPassword, userName, userCollege, userPhone);
        return Result.success("注册成功", user);
    }

    @GetMapping("/register")
    public Result<?> registerGet(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String name,
                                 @RequestParam(required = false) String college,
                                 @RequestParam(required = false) String phone) {
        return register(null, username, null, password, name, college, phone);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody(required = false) Map<String, String> body,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String account,
                           @RequestParam(required = false) String password) {

        String userAccount = (body != null && body.get("username") != null) ? body.get("username") :
                           (body != null && body.get("account") != null) ? body.get("account") :
                           (username != null ? username : account);

        String userPassword = (body != null && body.get("password") != null) ? body.get("password") : password;

        if (userAccount == null || userPassword == null) {
            return Result.error("参数不完整");
        }

        try {
            User user = userService.login(userAccount, userPassword);
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("userId", user.getUserId());
            return Result.success("登录成功", data);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/login")
    public Result<?> loginGet(@RequestParam String username, @RequestParam String password) {
        return login(null, username, null, password);
    }

    @GetMapping("/info/{userId}")
    public Result<?> getUserInfo(@PathVariable Long userId) {
        User user = userService.getUserInfo(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody User user) {
        if (user.getUserId() == null) {
            return Result.error("用户ID不能为空");
        }
        userService.updateById(user);
        return Result.success("更新成功");
    }

    @PutMapping("/update-profile")
    public Result<?> updateProfile(@RequestBody User user) {
        if (user.getUserId() == null) {
            return Result.error("用户ID不能为空");
        }
        userService.updateById(user);
        return Result.success("更新成功");
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, Object> params) {
        Long userId = ((Number) params.get("userId")).longValue();
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");

        if (userId == null || oldPassword == null || newPassword == null) {
            return Result.error("参数不完整");
        }

        try {
            userService.updatePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
