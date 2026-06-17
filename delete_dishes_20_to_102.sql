-- 完整删除脚本：处理外键约束
USE campus_canteen;

-- 1. 先查看要删除菜品相关的订单项
SELECT * FROM order_items WHERE dish_id BETWEEN 20 AND 102;

-- 2. 暂时关闭外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 3. 删除菜品相关的订单项（如果需要）
DELETE FROM order_items WHERE dish_id BETWEEN 20 AND 102;

-- 4. 删除菜品
DELETE FROM dishes WHERE dish_id BETWEEN 20 AND 102;

-- 5. 重新开启外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 6. 验证
SELECT COUNT(*) AS remaining_dishes FROM dishes;
SELECT COUNT(*) AS remaining_order_items FROM order_items;
