package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.UserRestrictions;
import com.campus.canteen.mapper.UserRestrictionsMapper;
import com.campus.canteen.service.UserRestrictionsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRestrictionsServiceImpl extends ServiceImpl<UserRestrictionsMapper, UserRestrictions> implements UserRestrictionsService {

    @Override
    public List<UserRestrictions> getUserRestrictions(Long userId) {
        LambdaQueryWrapper<UserRestrictions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRestrictions::getUserId, userId);
        return list(wrapper);
    }

    @Override
    public void saveUserRestriction(Long userId, Integer restrictionType, String restrictionDesc) {
        // 检查是否已存在相同类型的忌口
        LambdaQueryWrapper<UserRestrictions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRestrictions::getUserId, userId)
               .eq(UserRestrictions::getRestrictionType, restrictionType);
        UserRestrictions existing = getOne(wrapper);
        
        if (existing != null) {
            // 已存在，追加描述（用顿号分隔）
            String newDesc = existing.getRestrictionDesc() + "、" + restrictionDesc;
            existing.setRestrictionDesc(newDesc);
            updateById(existing);
        } else {
            // 新建
            UserRestrictions restriction = new UserRestrictions();
            restriction.setUserId(userId);
            restriction.setRestrictionType(restrictionType);
            restriction.setRestrictionDesc(restrictionDesc);
            save(restriction);
        }
    }

    @Override
    public void deleteUserRestriction(Long restrictionId) {
        removeById(restrictionId);
    }
}
