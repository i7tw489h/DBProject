package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ai_recommendations")
public class AIRecommendations {
    @TableId(type = IdType.AUTO)
    private Long recId;
    private Long userId;
    private Long dishId;
    private Integer recType;
    private BigDecimal score;
    private LocalDateTime createdAt;
}
