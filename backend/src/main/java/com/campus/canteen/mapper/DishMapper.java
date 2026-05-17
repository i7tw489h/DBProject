package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Nutrition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("SELECT * FROM v_dish_nutrition WHERE is_shelf = 1")
    List<Dish> selectAllWithNutrition();

    @Select("SELECT * FROM v_dish_nutrition WHERE category_id = #{categoryId} AND is_shelf = 1")
    List<Dish> selectByCategory(Long categoryId);

    @Select("SELECT d.* FROM dishes d JOIN windows w ON d.window_id = w.window_id " +
            "WHERE w.floor = #{floor} AND d.is_shelf = 1")
    List<Dish> selectByFloor(Integer floor);

    @Select("SELECT * FROM dishes WHERE window_id = #{windowId} AND is_shelf = 1")
    List<Dish> selectByWindow(Long windowId);

    @Select("SELECT * FROM dishes WHERE category_id = #{categoryId} AND window_id = #{windowId} AND is_shelf = 1")
    List<Dish> selectByCategoryAndWindow(Long categoryId, Long windowId);

    @Select("SELECT * FROM dishes WHERE category_id = #{categoryId} AND is_shelf = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByCategoryAndKeyword(Long categoryId, String keyword);

    @Select("SELECT * FROM dishes WHERE window_id = #{windowId} AND is_shelf = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByWindowAndKeyword(Long windowId, String keyword);

    @Select("SELECT * FROM dishes WHERE category_id = #{categoryId} AND window_id = #{windowId} AND is_shelf = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByCategoryWindowAndKeyword(Long categoryId, Long windowId, String keyword);

    @Select("SELECT * FROM dishes WHERE is_shelf = 1 AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByKeyword(String keyword);

    @Select("SELECT n.* FROM nutrition n WHERE n.dish_id = #{dishId}")
    Nutrition selectNutritionByDishId(Long dishId);
}