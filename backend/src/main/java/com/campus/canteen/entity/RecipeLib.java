package com.campus.canteen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("recipe_lib")
public class RecipeLib {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String recipeName;

    private String foodMaterial;

    private String cookStep;

    private Integer calorie;

    private String suitableTag;

    private LocalDateTime createTime;
}