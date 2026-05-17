package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Cart;
import com.campus.canteen.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list/{userId}")
    public Result<?> getCartList(@PathVariable Long userId) {
        List<Cart> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    @PostMapping("/add")
    public Result<?> addToCart(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Long dishId = Long.valueOf(params.get("dishId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        boolean success = cartService.addToCart(userId, dishId, quantity);
        return success ? Result.success("添加成功") : Result.error("添加失败");
    }

    @PutMapping("/update")
    public Result<?> updateCartQuantity(@RequestBody Map<String, Object> params) {
        Long cartId = Long.valueOf(params.get("cartId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        boolean success = cartService.updateCartQuantity(cartId, quantity);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{cartId}")
    public Result<?> removeFromCart(@PathVariable Long cartId) {
        boolean success = cartService.removeFromCart(cartId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    @DeleteMapping("/clear/{userId}")
    public Result<?> clearCart(@PathVariable Long userId) {
        boolean success = cartService.clearCart(userId);
        return success ? Result.success("清空成功") : Result.error("清空失败");
    }
}