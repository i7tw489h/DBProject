-- 检查视图 v_dish_nutrition 是否存在
USE campus_canteen;

-- 1. 查看视图是否存在
SHOW FULL TABLES WHERE Table_type = 'VIEW';

-- 2. 如果存在，查询数据
SELECT COUNT(*) AS total FROM v_dish_nutrition;

-- 3. 查看视图定义
SHOW CREATE VIEW v_dish_nutrition;

-- 4. 对比实际表数据
SELECT COUNT(*) AS dishes_total FROM dishes;
