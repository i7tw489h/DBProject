package com.campus.canteen.service;

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
    void acceptOrder(String orderId);
    void serveOrder(String orderId);
    void saveOrder(Order order, List<java.util.Map<String, Object>> items);
    Order getOrderById(String orderId);
}