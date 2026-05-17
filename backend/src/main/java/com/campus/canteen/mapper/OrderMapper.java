package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT o.*, u.name as userName FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.user_id = #{userId} ORDER BY o.created_at DESC")
    List<Order> selectByUserId(Long userId);

    @Select("SELECT o.*, u.name as userName FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.user_id = #{userId} AND o.status = #{status} ORDER BY o.created_at DESC")
    List<Order> selectByUserIdAndStatus(Long userId, Integer status);

    @Select("SELECT o.*, u.name as userName FROM orders o LEFT JOIN users u ON o.user_id = u.user_id ORDER BY o.created_at DESC")
    List<Order> selectAll();

    @Select("SELECT o.*, u.name as userName FROM orders o LEFT JOIN users u ON o.user_id = u.user_id WHERE o.status = #{status} ORDER BY o.created_at DESC")
    List<Order> selectAllByStatus(Integer status);
}
