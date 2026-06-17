package com.campus.canteen.service.impl;

import com.campus.canteen.entity.Order;
import com.campus.canteen.entity.OrderItem;
import com.campus.canteen.mapper.OrderMapper;
import com.campus.canteen.mapper.OrderItemMapper;
import com.campus.canteen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    // 菜品名称到图片路径的映射
    private static final Map<String, String> DISH_IMAGE_MAP = new HashMap<>();
    static {
        DISH_IMAGE_MAP.put("宫保鸡丁", "/images/dishes/1.jpg");
        DISH_IMAGE_MAP.put("鱼香肉丝", "/images/dishes/2.jpg");
        DISH_IMAGE_MAP.put("红烧肉", "/images/dishes/3.jpg");
        DISH_IMAGE_MAP.put("糖醋里脊", "/images/dishes/4.jpg");
        DISH_IMAGE_MAP.put("番茄炒蛋", "/images/dishes/番茄炒蛋.jpg");
        DISH_IMAGE_MAP.put("蒜蓉菠菜", "/images/dishes/蒜蓉菠菜.jpg");
        DISH_IMAGE_MAP.put("白米饭", "/images/dishes/白米饭.jpg");
        DISH_IMAGE_MAP.put("清炒西蓝花", "/images/dishes/清炒西蓝花.jpg");
        DISH_IMAGE_MAP.put("麻婆豆腐", "/images/dishes/麻婆豆腐.jpg");
        DISH_IMAGE_MAP.put("酸辣汤", "/images/dishes/酸辣汤.jpg");
        DISH_IMAGE_MAP.put("排骨汤", "/images/dishes/排骨汤.jpg");
        DISH_IMAGE_MAP.put("小米粥", "/images/dishes/小米粥.jpg");
        DISH_IMAGE_MAP.put("豆沙包", "/images/dishes/豆沙包.jpg");
        DISH_IMAGE_MAP.put("花卷", "/images/dishes/花卷.jpg");
    }

    // 自动填充订单明细中的图片路径
    private void fillOrderItemImageUrl(List<OrderItem> items) {
        if (items != null) {
            for (OrderItem item : items) {
                if (item.getImageUrl() == null || item.getImageUrl().isEmpty()) {
                    String imageUrl = DISH_IMAGE_MAP.get(item.getName());
                    if (imageUrl != null) {
                        item.setImageUrl(imageUrl);
                    }
                }
            }
        }
    }

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
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            fillOrderItemImageUrl(items);
            order.setItems(items);
        }
        
        return orders;
    }

    @Override
    public Order getOrderDetail(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            fillOrderItemImageUrl(items);
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
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            fillOrderItemImageUrl(items);
            order.setItems(items);
        }
        
        return orders;
    }

    @Override
    public List<Order> getAllOrders(Integer status, String pickupTime, Long windowId) {
        List<Order> orders = getAllOrders(status);
        
        if (pickupTime != null && !pickupTime.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> pickupTime.equals(order.getPickupTime()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return orders;
    }

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<Order> getOrdersByUserIdWithPage(Long userId, Integer status, int page, int pageSize) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
        com.baomidou.mybatisplus.core.metadata.IPage<Order> orderPage = orderMapper.selectByUserIdWithPage(pageParam, userId, status);
        
        for (Order order : orderPage.getRecords()) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            fillOrderItemImageUrl(items);
            order.setItems(items);
        }
        
        return orderPage;
    }

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<Order> getAllOrdersWithPage(Integer status, String pickupTime, Long windowId, int page, int pageSize) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
        com.baomidou.mybatisplus.core.metadata.IPage<Order> orderPage = orderMapper.selectAllWithPage(pageParam, status, pickupTime, windowId);
        
        for (Order order : orderPage.getRecords()) {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
            fillOrderItemImageUrl(items);
            order.setItems(items);
        }
        
        return orderPage;
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
    public void finishOrder(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            order.setStatus(4);
            order.setFinishTime(LocalDateTime.now());
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
            Integer quantity = (Integer) item.get("quantity");
            orderItem.setQuantity(quantity);
            java.math.BigDecimal price = new java.math.BigDecimal(item.get("price").toString());
            orderItem.setPrice(price);
            orderItem.setSubtotal(price.multiply(java.math.BigDecimal.valueOf(quantity)));
            orderItem.setName((String) item.get("name"));
            orderItem.setImageUrl((String) item.get("imageUrl"));
            orderItemMapper.insert(orderItem);
        }
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public void deleteOrder(String orderId) {
        orderItemMapper.deleteByOrderId(orderId);
        orderMapper.deleteById(orderId);
    }
}