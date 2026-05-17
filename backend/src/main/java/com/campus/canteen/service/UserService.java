package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.User;

public interface UserService extends IService<User> {
    User register(String account, String password, String name, String college, String phone);
    User login(String account, String password);
    User getUserInfo(Long userId);
}