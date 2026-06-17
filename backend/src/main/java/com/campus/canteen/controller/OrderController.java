package com.campus.canteen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Order;
import com.campus.canteen.entity.OrderItem;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.service.OrderService;
import com.campus.canteen.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DishService dishService;

    @PostMapping("/submit")
    public Result<?> submitOrder(@RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        String pickupTime = (String) body.get("pickupTime");
        String remark = (String) body.get("remark");

        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setUserId(userId);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setStatus(1);
        order.setPickupCode(generatePickupCode());
        order.setPickupTime(pickupTime);
        order.setRemark(remark);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            Long dishId = ((Number) item.get("dishId")).longValue();
            Integer quantity = (Integer) item.get("quantity");
            BigDecimal price = new BigDecimal(item.get("price").toString());

            Dish dish = dishService.getDishDetail(dishId);
            if (dish != null) {
                totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
            }
        }
        order.setTotalAmount(totalAmount);

        orderService.saveOrder(order, items);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getOrderId());
        result.put("pickupCode", order.getPickupCode());
        return Result.success(result);
    }

    @GetMapping("/user/{userId}")
    public Result<?> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId, null);
        return Result.success(orders);
    }

    @GetMapping("/{orderId}")
    public Result<?> getOrderDetail(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            return Result.success(order);
        }
        return Result.error("订单不存在");
    }

    @GetMapping("/list")
    public Result<?> getOrdersByUserWithStatus(
            @RequestParam Long userId, 
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page <= 0) page = 1;
        if (pageSize <= 0 || pageSize > 100) pageSize = 10;
        
        if (page == 1 && pageSize >= 1000) {
            List<Order> orders = orderService.getOrdersByUserId(userId, status);
            return Result.success(orders);
        }
        
        IPage<Order> orderPage = orderService.getOrdersByUserIdWithPage(userId, status, page, pageSize);
        return Result.success(orderPage);
    }

    @GetMapping("/all")
    public Result<?> getAllOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String pickupTime,
            @RequestParam(required = false) Long windowId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (page <= 0) page = 1;
        if (pageSize <= 0 || pageSize > 100) pageSize = 10;
        
        if (page == 1 && pageSize >= 1000) {
            List<Order> orders = orderService.getAllOrders(status, pickupTime, windowId);
            return Result.success(orders);
        }
        
        IPage<Order> orderPage = orderService.getAllOrdersWithPage(status, pickupTime, page, pageSize);
        return Result.success(orderPage);
    }

    @PutMapping("/accept/{orderId}")
    public Result<?> acceptOrder(@PathVariable String orderId) {
        orderService.acceptOrder(orderId);
        return Result.success("接单成功");
    }

    @PutMapping("/serve/{orderId}")
    public Result<?> serveOrder(@PathVariable String orderId) {
        orderService.serveOrder(orderId);
        return Result.success("出餐成功");
    }

    @PutMapping("/finish/{orderId}")
    public Result<?> finishOrder(@PathVariable String orderId) {
        orderService.finishOrder(orderId);
        return Result.success("取餐成功，订单已完成");
    }

    @PutMapping("/cancel/{orderId}")
    public Result<?> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return Result.success("取消成功");
    }

    @DeleteMapping("/{orderId}")
    public Result<?> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return Result.success("删除成功");
    }

    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private String generatePickupCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}