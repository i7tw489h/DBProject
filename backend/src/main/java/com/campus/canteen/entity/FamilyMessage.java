package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("family_message")
public class FamilyMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long senderUserId;

    private Long receiverUserId;

    private String msgContent;

    private Integer isRead;

    private LocalDateTime createTime;
}
