package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.SmokeAlcoholRecord;
import com.campus.canteen.mapper.SmokeAlcoholRecordMapper;
import com.campus.canteen.service.SmokeAlcoholRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SmokeAlcoholRecordServiceImpl extends ServiceImpl<SmokeAlcoholRecordMapper, SmokeAlcoholRecord> implements SmokeAlcoholRecordService {

    private static final Logger logger = LoggerFactory.getLogger(SmokeAlcoholRecordServiceImpl.class);

    @Override
    public List<SmokeAlcoholRecord> listByUserId(Long userId) {
        logger.info("查询用户烟酒记录 userId={}",userId);
        LambdaQueryWrapper<SmokeAlcoholRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmokeAlcoholRecord::getUserId,userId);
        return this.baseMapper.selectList(wrapper);
    }
}