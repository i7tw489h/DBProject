package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("user_restrictions")
public class UserRestrictions {
    @TableId(type = IdType.AUTO)
    private Long restrictionId;
    private Long userId;
    private Integer restrictionType;
    private String restrictionDesc;
    private Timestamp createdAt;

    public String getRestrictionValue() {
        return restrictionDesc;
    }
}
