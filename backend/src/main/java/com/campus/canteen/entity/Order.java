package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.INPUT)
    private String orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status;
    private String pickupCode;
    private String pickupTime;
    private String remark;
    @TableField(exist = false)
    private List<OrderItem> items;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String phone;
    private LocalDateTime paymentTime;
    private LocalDateTime acceptTime;
    private LocalDateTime serveTime;
    private LocalDateTime cancelTime;
    private LocalDateTime finishTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}