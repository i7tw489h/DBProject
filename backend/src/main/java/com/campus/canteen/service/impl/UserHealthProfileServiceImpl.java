package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.UserHealthProfile;
import com.campus.canteen.mapper.UserHealthProfileMapper;
import com.campus.canteen.service.UserHealthProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserHealthProfileServiceImpl extends ServiceImpl<UserHealthProfileMapper, UserHealthProfile> implements UserHealthProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserHealthProfileServiceImpl.class);

    @Override
    public UserHealthProfile getByUserId(Long userId) {
        logger.info("查询用户健康档案 userId={}",userId);
        LambdaQueryWrapper<UserHealthProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserHealthProfile::getUserId,userId);
        UserHealthProfile profile = this.baseMapper.selectOne(wrapper);
        return profile;
    }
}