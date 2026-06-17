package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Nutrition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("SELECT * FROM v_dish_nutrition")
    List<Dish> selectAllWithNutrition();

    @Select("SELECT * FROM v_dish_nutrition WHERE category_id = #{categoryId}")
    List<Dish> selectByCategoryWithNutrition(Long categoryId);

    @Select("SELECT * FROM v_dish_nutrition WHERE window_id = #{windowId}")
    List<Dish> selectByWindowWithNutrition(Long windowId);

    @Select("SELECT * FROM v_dish_nutrition WHERE category_id = #{categoryId} AND window_id = #{windowId}")
    List<Dish> selectByCategoryAndWindowWithNutrition(Long categoryId, Long windowId);

    @Select("SELECT * FROM v_dish_nutrition WHERE name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')")
    List<Dish> selectByKeywordWithNutrition(String keyword);

    @Select("SELECT * FROM v_dish_nutrition WHERE category_id = #{categoryId} AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByCategoryAndKeywordWithNutrition(Long categoryId, String keyword);

    @Select("SELECT * FROM v_dish_nutrition WHERE window_id = #{windowId} AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByWindowAndKeywordWithNutrition(Long windowId, String keyword);

    @Select("SELECT * FROM v_dish_nutrition WHERE category_id = #{categoryId} AND window_id = #{windowId} AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))")
    List<Dish> selectByCategoryWindowAndKeywordWithNutrition(Long categoryId, Long windowId, String keyword);

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

    /**
     * 调用存储函数计算健康评级
     */
    @Select("SELECT fn_calc_health_rating(#{calories}, #{protein}, #{fat}, #{sodium})")
    String calcHealthRating(@Param("calories") java.math.BigDecimal calories,
                            @Param("protein") java.math.BigDecimal protein,
                            @Param("fat") java.math.BigDecimal fat,
                            @Param("sodium") java.math.BigDecimal sodium);

    /**
     * 调用存储函数计算推荐度评分（用于猜你喜欢组）
     */
    @Select("SELECT fn_calc_recommend_score(#{calories}, #{protein}, #{fat}, #{sodium})")
    java.math.BigDecimal calcRecommendScore(@Param("calories") java.math.BigDecimal calories,
                                            @Param("protein") java.math.BigDecimal protein,
                                            @Param("fat") java.math.BigDecimal fat,
                                            @Param("sodium") java.math.BigDecimal sodium);

    /**
     * 分页查询菜品，支持筛选与随机排序
     */
    @Select({
        "<script>",
        "SELECT * FROM dishes WHERE is_shelf = 1",
        "<if test='categoryId != null and categoryId > 0'> AND category_id = #{categoryId}</if>",
        "<if test='windowId != null and windowId > 0'> AND window_id = #{windowId}</if>",
        "<if test='keyword != null and keyword != \"\"'> AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))</if>",
        "ORDER BY RAND()",
        "</script>"
    })
    IPage<Dish> selectPageWithFilter(Page<Dish> page,
                                     @Param("categoryId") Long categoryId,
                                     @Param("windowId") Long windowId,
                                     @Param("keyword") String keyword);
}