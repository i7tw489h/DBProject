package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {
    List<Cart> getCartList(Long userId);
    boolean addToCart(Long userId, Long dishId, Integer quantity);
    boolean updateCartQuantity(Long cartId, Integer quantity);
    boolean removeFromCart(Long cartId);
    boolean clearCart(Long userId);
}