package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_restrictions")
public class UserRestriction {
    @TableId(type = IdType.AUTO)
    private Long restrictionId;
    private Long userId;
    private Integer restrictionType;
    private String restrictionDesc;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}