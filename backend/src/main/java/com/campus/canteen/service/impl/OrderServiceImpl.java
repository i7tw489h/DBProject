package com.campus.canteen.service.impl;

import com.campus.canteen.entity.Order;
import com.campus.canteen.entity.OrderItem;
import com.campus.canteen.mapper.OrderMapper;
import com.campus.canteen.mapper.OrderItemMapper;
import com.campus.canteen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public Order save(Order order) {
        orderMapper.insert(order);
        return order;
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId, Object status) {
        List<Order> orders;
        if (status == null) {
            orders = orderMapper.selectByUserId(userId);
        } else if (status instanceof List) {
            orders = orderMapper.selectByUserId(userId);
            List<Integer> statusList = (List<Integer>) status;
            orders = orders.stream()
                    .filter(order -> statusList.contains(order.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        } else {
            orders = orderMapper.selectByUserIdAndStatus(userId, ((Number) status).intValue());
        }
        
        for (Order order : orders) {
            order.setItems(orderItemMapper.selectByOrderId(order.getOrderId()));
        }
        
        return orders;
    }

    @Override
    public Order getOrderDetail(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            order.setItems(items);
        }
        return order;
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(5);
            order.setCancelTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public List<Order> getAllOrders(Integer status) {
        List<Order> orders;
        if (status == null) {
            orders = orderMapper.selectAll();
        } else {
            orders = orderMapper.selectAllByStatus(status);
        }
        
        for (Order order : orders) {
            order.setItems(orderItemMapper.selectByOrderId(order.getOrderId()));
        }
        
        return orders;
    }

    @Override
    public void acceptOrder(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(2);
            order.setAcceptTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void serveOrder(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(3);
            order.setServeTime(LocalDateTime.now());
            orderMapper.updateById(order);
        }
    }

    @Override
    public void saveOrder(Order order, List<java.util.Map<String, Object>> items) {
        orderMapper.insert(order);
        
        for (java.util.Map<String, Object> item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setDishId(((Number) item.get("dishId")).longValue());
            orderItem.setQuantity((Integer) item.get("quantity"));
            orderItem.setPrice(new java.math.BigDecimal(item.get("price").toString()));
            orderItem.setName((String) item.get("name"));
            orderItem.setImageUrl((String) item.get("imageUrl"));
            orderItemMapper.insert(orderItem);
        }
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderMapper.selectById(orderId);
    }
}