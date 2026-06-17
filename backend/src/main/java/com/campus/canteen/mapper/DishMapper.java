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

    @Select("SELECT name FROM categories WHERE category_id = #{categoryId}")
    String getCategoryName(Long categoryId);

    @Select("SELECT name FROM windows WHERE window_id = #{windowId}")
    String getWindowName(Long windowId);
    
    @Select("SELECT MAX(dish_id) FROM dishes")
    Long selectMaxId();

    /**
     * 调用存储函数计算菜品推荐分数
     * @param favoriteCount 收藏数
     * @param browseCount 浏览数
     * @param orderCount 订单数
     * @param salesCount 销量
     * @return 推荐分数
     */
    @Select("SELECT fn_calculate_recommend_score(#{favoriteCount}, #{browseCount}, #{orderCount}, #{salesCount})")
    java.math.BigDecimal calculateRecommendScore(
            @org.apache.ibatis.annotations.Param("favoriteCount") Integer favoriteCount,
            @org.apache.ibatis.annotations.Param("browseCount") Integer browseCount,
            @org.apache.ibatis.annotations.Param("orderCount") Integer orderCount,
            @org.apache.ibatis.annotations.Param("salesCount") Integer salesCount
    );

    /**
     * 调用存储函数获取菜品营养等级
     * @param calories 卡路里
     * @param protein 蛋白质(g)
     * @param fat 脂肪(g)
     * @param goal 用户目标(1-减脂, 2-增肌, 3-均衡)
     * @return 营养等级(优秀/良好/一般/不合适)
     */
    @Select("SELECT fn_get_nutrition_level(#{calories}, #{protein}, #{fat}, #{goal})")
    String getNutritionLevel(
            @org.apache.ibatis.annotations.Param("calories") java.math.BigDecimal calories,
            @org.apache.ibatis.annotations.Param("protein") java.math.BigDecimal protein,
            @org.apache.ibatis.annotations.Param("fat") java.math.BigDecimal fat,
            @org.apache.ibatis.annotations.Param("goal") Integer goal
    );
}