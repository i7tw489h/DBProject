package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ai_sales_prediction")
public class SalesPrediction {

    @TableId(type = IdType.AUTO)
    private Long predId;

    private Long dishId;

    private LocalDate predictDate;

    private Integer predictedSales;

    private Integer actualSales;

    private BigDecimal confidence;

    private LocalDateTime createdAt;

    // 扩展字段（不存储在数据库）
    @TableField(exist = false)
    private String dishName;
    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private BigDecimal price;
}
