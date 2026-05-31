-- 添加今天(2026-05-29)的测试订单数据
-- 用户11，已完成订单

INSERT INTO orders 
(order_id, user_id, total_amount, status, pickup_code, pickup_time, remark, payment_time, accept_time, serve_time, cancel_time, finish_time, created_at, updated_at)
VALUES 
('ORD1779987654321099', 11, 46.00, 4, '876544', '12:30-13:00', NULL, NOW(), NOW(), NOW(), NULL, '2026-05-29 12:30:00', NOW(), NOW());

INSERT INTO order_items 
(order_id, dish_id, name, image_url, quantity, price, subtotal, window_id, created_at)
VALUES 
('ORD1779987654321099', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, 2, NOW()),
('ORD1779987654321099', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, 1, NOW()),
('ORD1779987654321099', 11, '白米饭', '/images/dishes/白米饭.jpg', 2, 2.00, 4.00, 4, NOW()),
('ORD1779987654321099', 3, '红烧肉', '/images/dishes/3.jpg', 1, 20.00, 20.00, 1, NOW());

SELECT '订单已创建成功' AS message;
SELECT order_id, user_id, status, finish_time FROM orders WHERE order_id = 'ORD1779987654321099';
