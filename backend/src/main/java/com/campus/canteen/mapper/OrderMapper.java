package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.canteen.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT o.*, u.name as userName, u.phone FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.user_id = #{userId} ORDER BY o.created_at DESC")
    List<Order> selectByUserId(Long userId);

    @Select("SELECT o.*, u.name as userName, u.phone FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.user_id = #{userId} AND o.status = #{status} ORDER BY o.created_at DESC")
    List<Order> selectByUserIdAndStatus(Long userId, Integer status);

    @Select("SELECT o.*, u.name as userName, u.phone FROM orders o LEFT JOIN users u ON o.user_id = u.user_id ORDER BY o.created_at DESC")
    List<Order> selectAll();

    @Select("SELECT o.*, u.name as userName, u.phone FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.status = #{status} ORDER BY o.created_at DESC")
    List<Order> selectAllByStatus(Integer status);

    @Select("SELECT o.*, u.name as userName, u.phone FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.user_id = #{userId} AND (#{status} IS NULL OR o.status = #{status}) ORDER BY o.created_at DESC")
    IPage<Order> selectByUserIdWithPage(Page<Order> page, @Param("userId") Long userId, @Param("status") Integer status);

    @Select("SELECT o.*, u.name as userName, u.phone FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE (#{status} IS NULL OR o.status = #{status}) AND (#{pickupTime} IS NULL OR o.pickup_time = #{pickupTime}) AND (#{windowId} IS NULL OR EXISTS (SELECT 1 FROM order_items oi JOIN dishes d ON oi.dish_id = d.dish_id WHERE oi.order_id = o.order_id AND d.window_id = #{windowId})) ORDER BY o.created_at DESC")
    IPage<Order> selectAllWithPage(Page<Order> page, @Param("status") Integer status, @Param("pickupTime") String pickupTime, @Param("windowId") Long windowId);
}
