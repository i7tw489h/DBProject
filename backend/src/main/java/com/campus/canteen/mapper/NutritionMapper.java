package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Nutrition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NutritionMapper extends BaseMapper<Nutrition> {
    @Select("SELECT * FROM nutrition WHERE dish_id = #{dishId}")
    Nutrition selectNutritionByDishId(Long dishId);
}