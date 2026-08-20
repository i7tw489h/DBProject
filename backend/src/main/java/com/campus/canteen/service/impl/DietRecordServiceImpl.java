package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.DietRecord;
import com.campus.canteen.mapper.DietRecordMapper;
import com.campus.canteen.service.DietRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DietRecordServiceImpl extends ServiceImpl<DietRecordMapper, DietRecord> implements DietRecordService {

    private static final Logger logger = LoggerFactory.getLogger(DietRecordServiceImpl.class);

    @Override
    public List<DietRecord> listByUserId(Long userId) {
        logger.info("查询用户饮食记录 userId={}",userId);
        LambdaQueryWrapper<DietRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DietRecord::getUserId,userId);
        return this.baseMapper.selectList(wrapper);
    }
}
