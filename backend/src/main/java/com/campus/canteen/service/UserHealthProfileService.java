package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.UserHealthProfile;

public interface UserHealthProfileService extends IService<UserHealthProfile> {
    UserHealthProfile getByUserId(Long userId);
}