package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @Select("SELECT c.*, d.name as dish_name, d.price as dish_price, d.image_url " +
            "FROM cart c JOIN dishes d ON c.dish_id = d.dish_id " +
            "WHERE c.user_id = #{userId}")
    List<Cart> selectCartWithDish(Long userId);
}