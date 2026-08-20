package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.PetScore;
import com.campus.canteen.mapper.PetScoreMapper;
import com.campus.canteen.service.PetScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PetScoreServiceImpl extends ServiceImpl<PetScoreMapper, PetScore> implements PetScoreService {

    private static final Logger logger = LoggerFactory.getLogger(PetScoreServiceImpl.class);

    @Override
    public PetScore getByUserId(Long userId) {
        logger.info("查询用户宠物积分 userId={}",userId);
        LambdaQueryWrapper<PetScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetScore::getUserId,userId);
        return this.baseMapper.selectOne(wrapper);
    }
}