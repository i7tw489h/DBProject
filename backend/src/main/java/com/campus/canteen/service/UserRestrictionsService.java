package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.UserRestrictions;

import java.util.List;

public interface UserRestrictionsService extends IService<UserRestrictions> {
    List<UserRestrictions> getUserRestrictions(Long userId);
    void saveUserRestriction(Long userId, Integer restrictionType, String restrictionDesc);
    void deleteUserRestriction(Long restrictionId);
}
