package com.campus.canteen.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("smoke_alcohol_record")
public class SmokeAlcoholRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDateTime recordTime;

    private Integer smokeCount;

    private Integer alcoholVolume;

    private String riskTip;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}