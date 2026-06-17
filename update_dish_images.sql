-- 批量更新菜品图片脚本
-- 在 Navicat 中执行
USE campus_canteen;

-- 更新菜品图片（根据菜品名称匹配对应的图片）
-- 图片路径格式：/images/dishes/xxx.jpg

-- 已有具体名称的图片
UPDATE dishes SET image_url = '/images/dishes/麻婆豆腐.jpg' WHERE name = '麻婆豆腐';
UPDATE dishes SET image_url = '/images/dishes/酸辣汤.jpg' WHERE name = '酸辣汤';
UPDATE dishes SET image_url = '/images/dishes/豆沙包.jpg' WHERE name = '豆沙包';
UPDATE dishes SET image_url = '/images/dishes/蒜蓉菠菜.jpg' WHERE name = '蒜蓉菠菜';
UPDATE dishes SET image_url = '/images/dishes/花卷.jpg' WHERE name = '花卷';
UPDATE dishes SET image_url = '/images/dishes/白米饭.jpg' WHERE name = '白米饭';
UPDATE dishes SET image_url = '/images/dishes/番茄炒蛋.jpg' WHERE name = '番茄炒蛋';
UPDATE dishes SET image_url = '/images/dishes/清炒西蓝花.jpg' WHERE name = '清炒西蓝花';
UPDATE dishes SET image_url = '/images/dishes/排骨汤.jpg' WHERE name = '排骨汤';
UPDATE dishes SET image_url = '/images/dishes/小米粥.jpg' WHERE name = '小米粥';

-- 模糊匹配（处理名称相似的情况）
UPDATE dishes SET image_url = '/images/dishes/麻婆豆腐.jpg' WHERE name LIKE '%麻婆%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/酸辣汤.jpg' WHERE name LIKE '%酸辣%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/番茄炒蛋.jpg' WHERE name LIKE '%番茄%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/蒜蓉菠菜.jpg' WHERE name LIKE '%菠菜%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/清炒西蓝花.jpg' WHERE name LIKE '%西蓝花%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/排骨汤.jpg' WHERE name LIKE '%排骨%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/小米粥.jpg' WHERE name LIKE '%小米%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/花卷.jpg' WHERE name LIKE '%花卷%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/豆沙包.jpg' WHERE name LIKE '%豆沙%' AND image_url NOT LIKE '/images/dishes/%';
UPDATE dishes SET image_url = '/images/dishes/白米饭.jpg' WHERE name LIKE '%米饭%' AND image_url NOT LIKE '/images/dishes/%';

-- 显示所有菜品及其当前图片路径
SELECT id, name, image_url FROM dishes ORDER BY id;
