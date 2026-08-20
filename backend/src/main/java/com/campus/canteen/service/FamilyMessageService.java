package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.FamilyMessage;
import java.util.List;

public interface FamilyMessageService extends IService<FamilyMessage> {
    List<FamilyMessage> listByReceiverUserId(Long receiverUserId);
}
