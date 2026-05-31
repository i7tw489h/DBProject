package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_browsing")
public class UserBrowsing {
    @TableId(type = IdType.AUTO)
    private Long browsingId;
    private Long userId;
    private Long dishId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime browsingTime;
}