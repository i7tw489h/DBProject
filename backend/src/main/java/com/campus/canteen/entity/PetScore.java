package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("pet_score")
public class PetScore {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer totalScore;

    private String petStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}