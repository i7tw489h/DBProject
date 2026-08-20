package com.campus.canteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.campus.canteen.entity.FamilyRelation;
import com.campus.canteen.entity.FamilyMessage;
import com.campus.canteen.service.FamilyRelationService;
import com.campus.canteen.service.FamilyMessageService;
import com.campus.canteen.common.Result;
import java.util.List;

@RestController
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyRelationService familyRelationService;

    @Autowired
    private FamilyMessageService familyMessageService;

    //绑定亲属
    @PostMapping("/bind")
    public Result bindFamily(@RequestBody FamilyRelation familyRelation){
        familyRelationService.save(familyRelation);
        return Result.success();
    }

    //查询我的绑定亲属列表
    @GetMapping("/relation/list/{masterUserId}")
    public Result<List<FamilyRelation>> getBindList(@PathVariable Long masterUserId){
        List<FamilyRelation> list = familyRelationService.listByMasterUserId(masterUserId);
        return Result.success(list);
    }

    //发送家庭消息
    @PostMapping("/msg/send")
    public Result sendMsg(@RequestBody FamilyMessage familyMessage){
        familyMessageService.save(familyMessage);
        return Result.success();
    }

    //获取我的接收消息列表
    @GetMapping("/msg/list/{receiverUserId}")
    public Result<List<FamilyMessage>> getMsgList(@PathVariable Long receiverUserId){
        List<FamilyMessage> list = familyMessageService.listByReceiverUserId(receiverUserId);
        return Result.success(list);
    }

    //标记消息已读
    @PostMapping("/msg/read/{msgId}")
    public Result readMsg(@PathVariable Long msgId){
        FamilyMessage msg = familyMessageService.getById(msgId);
        msg.setIsRead(1);
        familyMessageService.updateById(msg);
        return Result.success();
    }
}