package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.PetScore;

public interface PetScoreService extends IService<PetScore> {
    PetScore getByUserId(Long userId);
}
