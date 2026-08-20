package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_health_profile")
public class UserHealthProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String targetTags; //逗号分隔 "减脂,高血压"
    private BigDecimal height;
    private BigDecimal weight;
    private LocalDateTime createTime;
}
