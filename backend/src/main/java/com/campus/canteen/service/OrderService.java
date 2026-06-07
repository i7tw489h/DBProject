package com.campus.canteen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.canteen.entity.Order;
import com.campus.canteen.entity.OrderItem;

import java.util.List;

public interface OrderService {
    Order save(Order order);
    void saveOrderItem(OrderItem orderItem);
    List<Order> getOrdersByUserId(Long userId, Object status);
    Order getOrderDetail(String orderId);
    void cancelOrder(String orderId);
    List<Order> getAllOrders(Integer status);
    List<Order> getAllOrders(Integer status, String pickupTime, Long windowId);
    void acceptOrder(String orderId);
    void serveOrder(String orderId);
    void finishOrder(String orderId);
    void saveOrder(Order order, List<java.util.Map<String, Object>> items);
    Order getOrderById(String orderId);
    void deleteOrder(String orderId);
    IPage<Order> getOrdersByUserIdWithPage(Long userId, Integer status, int page, int pageSize);
    IPage<Order> getAllOrdersWithPage(Integer status, String pickupTime, int page, int pageSize);
}