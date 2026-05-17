package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dishes")
public class Dish {
    @TableId(type = IdType.AUTO)
    private Long dishId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private String ingredients;
    private Long categoryId;
    private Long windowId;
    private Integer stock;
    private Integer isShelf;
    private Integer salesCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}