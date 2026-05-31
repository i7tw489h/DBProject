package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_items")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long itemId;
    private String orderId;
    @TableField(exist = false)
    private Long userId;
    private Long dishId;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}