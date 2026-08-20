package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.FamilyRelation;
import com.campus.canteen.mapper.FamilyRelationMapper;
import com.campus.canteen.service.FamilyRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FamilyRelationServiceImpl extends ServiceImpl<FamilyRelationMapper, FamilyRelation> implements FamilyRelationService {

    private static final Logger logger = LoggerFactory.getLogger(FamilyRelationServiceImpl.class);

    @Override
    public List<FamilyRelation> listByMasterUserId(Long masterUserId) {
        logger.info("查询主用户绑定亲属 masterUserId={}",masterUserId);
        LambdaQueryWrapper<FamilyRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyRelation::getMasterUserId,masterUserId);
        return this.baseMapper.selectList(wrapper);
    }
}