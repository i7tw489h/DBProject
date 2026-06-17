package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.SalesPrediction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SalesPredictionMapper extends BaseMapper<SalesPrediction> {

    @Select("SELECT dish_id, COUNT(*) as sales_count " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "WHERE DATE(o.created_at) >= #{startDate} " +
            "AND DATE(o.created_at) <= #{endDate} " +
            "GROUP BY dish_id " +
            "ORDER BY sales_count DESC")
    List<DishSalesVO> getDishSalesByDateRange(LocalDate startDate, LocalDate endDate);

    @Select("SELECT dish_id, COUNT(*) as sales_count " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "WHERE DAYOFWEEK(o.created_at) = #{dayOfWeek} " +
            "AND DATE(o.created_at) >= DATE_SUB(CURRENT_DATE, INTERVAL 4 WEEK) " +
            "GROUP BY dish_id " +
            "ORDER BY sales_count DESC")
    List<DishSalesVO> getDishSalesByDayOfWeek(Integer dayOfWeek);

    @Select("SELECT dish_id, SUM(quantity) as total_quantity " +
            "FROM order_items " +
            "GROUP BY dish_id " +
            "ORDER BY total_quantity DESC")
    List<DishSalesVO> getAllTimePopularDishes();

    interface DishSalesVO {
        Long getDishId();
        Integer getSalesCount();
    }
}
