package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    @Select("SELECT oi.item_id, oi.order_id, oi.dish_id, d.name as name, oi.image_url, oi.quantity, oi.price, oi.subtotal, oi.window_id, oi.created_at FROM order_items oi LEFT JOIN dishes d ON oi.dish_id = d.dish_id WHERE oi.order_id = #{orderId}")
    List<OrderItem> selectByOrderId(String orderId);
    
    @org.apache.ibatis.annotations.Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    void deleteByOrderId(String orderId);
}
