package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.User;
import com.campus.canteen.mapper.UserMapper;
import com.campus.canteen.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User register(String account, String password, String name, String college, String phone) {
        try {
            logger.info("开始注册用户: account={}, name={}", account, name);

            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getAccount, account);
            Long count = this.baseMapper.selectCount(wrapper);
            logger.info("账号检查结果: count={}", count);

            if (count > 0) {
                throw new RuntimeException("账号已存在");
            }

            User user = new User();
            user.setAccount(account);
            String encryptedPassword = md5(password);
            logger.info("密码加密结果: length={}", encryptedPassword.length());
            user.setPassword(encryptedPassword);
            user.setName(name);
            user.setCollege(college);
            user.setPhone(phone);

            logger.info("准备插入用户数据: {}", user.toString());
            int result = this.baseMapper.insert(user);
            logger.info("插入结果: result={}, userId={}", result, user.getUserId());

            return user;
        } catch (Exception e) {
            logger.error("注册失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public User login(String account, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount, account);
        User user = this.baseMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        if (!user.getPassword().equals(md5(password))) {
            throw new RuntimeException("密码错误");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = this.baseMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String encryptedOldPassword = md5(oldPassword);
        if (!user.getPassword().equals(encryptedOldPassword)) {
            throw new RuntimeException("原密码错误");
        }

        String encryptedNewPassword = md5(newPassword);
        user.setPassword(encryptedNewPassword);
        this.baseMapper.updateById(user);
    }

    public String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败");
        }
    }
}
