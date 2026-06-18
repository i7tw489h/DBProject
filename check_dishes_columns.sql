-- 先查询 dishes 表的表结构
USE campus_canteen;

-- 查看表结构
DESC dishes;

-- 或者用 information_schema 查看
SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_KEY
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'campus_canteen' AND TABLE_NAME = 'dishes';
