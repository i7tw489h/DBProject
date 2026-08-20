package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.User;

public interface UserService extends IService<User> {
    User register(String username, String password, String name, String college, String phone);
    User login(String username, String password);
    User getUserInfo(Long userId);

    void updatePassword(Long userId, String oldPassword, String newPassword);
}
