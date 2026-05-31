package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("favorites")
public class Favorite {
    @TableId(type = IdType.AUTO)
    private Long favoriteId;
    private Long userId;
    private Long dishId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}