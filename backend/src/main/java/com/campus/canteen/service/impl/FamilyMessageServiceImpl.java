package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.FamilyMessage;
import com.campus.canteen.mapper.FamilyMessageMapper;
import com.campus.canteen.service.FamilyMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FamilyMessageServiceImpl extends ServiceImpl<FamilyMessageMapper, FamilyMessage> implements FamilyMessageService {

    private static final Logger logger = LoggerFactory.getLogger(FamilyMessageServiceImpl.class);

    @Override
    public List<FamilyMessage> listByReceiverUserId(Long receiverUserId) {
        logger.info("查询接收用户家庭消息 receiverUserId={}",receiverUserId);
        LambdaQueryWrapper<FamilyMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyMessage::getReceiverUserId,receiverUserId);
        return this.baseMapper.selectList(wrapper);
    }
}