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
    @TableField("is_shelf")
    private Integer isShelf;
    private Integer salesCount;
    @TableField(exist = false)
    private BigDecimal calories;
    @TableField(exist = false)
    private BigDecimal protein;
    @TableField(exist = false)
    private BigDecimal fat;
    @TableField(exist = false)
    private BigDecimal carbs;
    @TableField(exist = false)
    private BigDecimal sodium;
    @TableField(exist = false)
    private BigDecimal fiber;
    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private Double recommendScore;
    @TableField(exist = false)
    private String nutritionLevel;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}