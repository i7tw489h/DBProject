package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Cart;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.mapper.CartMapper;
import com.campus.canteen.mapper.DishMapper;
import com.campus.canteen.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public java.util.List<Cart> getCartList(Long userId) {
        return baseMapper.selectCartWithDish(userId);
    }

    @Override
    @Transactional
    public boolean addToCart(Long userId, Long dishId, Integer quantity) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getDishId, dishId);
        Cart existCart = this.baseMapper.selectOne(wrapper);

        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + quantity);
            this.baseMapper.updateById(existCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setDishId(dishId);
            cart.setQuantity(quantity);
            this.baseMapper.insert(cart);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateCartQuantity(Long cartId, Integer quantity) {
        if (quantity <= 0) {
            this.baseMapper.deleteById(cartId);
        } else {
            Cart cart = new Cart();
            cart.setCartId(cartId);
            cart.setQuantity(quantity);
            this.baseMapper.updateById(cart);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean removeFromCart(Long cartId) {
        this.baseMapper.deleteById(cartId);
        return true;
    }

    @Override
    @Transactional
    public boolean clearCart(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        this.baseMapper.delete(wrapper);
        return true;
    }
}