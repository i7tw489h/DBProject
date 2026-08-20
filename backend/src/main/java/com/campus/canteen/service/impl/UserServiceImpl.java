package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.User;
import com.campus.canteen.mapper.UserMapper;
import com.campus.canteen.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public User login(String username, String password) {
        logger.info("登录：username={}",username);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        User user = baseMapper.selectOne(wrapper);
        if(user == null){
            throw new RuntimeException("账号不存在");
        }
        //明文比对
        if(!user.getPassword().equals(password)){
            throw new RuntimeException("密码错误");
        }
        return user;
    }


    @Override
    public User register(String account, String password, String name, String college, String phone) {
        logger.info("注册账号：{}",account);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,account);
        User exist = baseMapper.selectOne(wrapper);
        if(exist != null){
            throw new RuntimeException("账号已存在");
        }
        User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        user.setName(name);
        user.setCollege(college);
        user.setPhone(phone);
        baseMapper.insert(user);
        return user;
    }

    // =========补上这个接口要求的方法=========
    @Override
    public User getUserInfo(Long userId) {
        return baseMapper.selectById(userId);
    }
    /**
     * 修改密码，明文版本
     */
    @Override
    public void updatePassword(Long userId, String oldPwd, String newPwd) {
        User user = baseMapper.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        //直接明文对比旧密码
        if(!user.getPassword().equals(oldPwd)){
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(newPwd);

    }
}