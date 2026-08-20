package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.FamilyRelation;
import java.util.List;

public interface FamilyRelationService extends IService<FamilyRelation> {
    List<FamilyRelation> listByMasterUserId(Long masterUserId);
}
