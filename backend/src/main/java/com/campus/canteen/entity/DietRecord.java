package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("diet_record")
public class DietRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDateTime eatTime;

    private Integer mealType;

    private String foodContent;

    private Integer calorie;

    private String riskTip;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}