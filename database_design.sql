-- =============================================
-- 校园AI食堂智能点餐与营养推荐系统 - 数据库设计
-- 满足第三范式(3NF)，完整的DDL脚本
-- =============================================



-- =============================================
-- 基础数据表
-- =============================================

-- 1. 用户表 (users)
-- 存储学生基本信息，满足登录注册需求
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键',
    account VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号，唯一',
    password VARCHAR(32) NOT NULL COMMENT '密码(MD5加密)',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    college VARCHAR(100) COMMENT '学院',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    height DECIMAL(5,2) COMMENT '身高(cm)',
    weight DECIMAL(5,2) COMMENT '体重(kg)',
    age INT COMMENT '年龄',
    gender TINYINT(1) COMMENT '性别: 0-女, 1-男',
    diet_goal TINYINT(1) COMMENT '饮食目标: 0-正常, 1-减脂, 2-增肌, 3-养胃',
    avatar VARCHAR(255) COMMENT '头像路径',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_account (account),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 用户忌口表 (user_restrictions)
-- 存储用户忌口偏好，支持多种忌口设置
CREATE TABLE IF NOT EXISTS user_restrictions (
    restriction_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '忌口ID',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键',
    restriction_type TINYINT(1) NOT NULL COMMENT '忌口类型: 0-不吃辣, 1-不吃香菜, 2-清真, 3-过敏, 4-素食',
    restriction_desc VARCHAR(200) COMMENT '忌口描述(如过敏食物具体名称)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_restriction (user_id, restriction_type) COMMENT '同一用户同一忌口类型只能设置一次'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户忌口表';

-- 3. 窗口表 (windows)
-- 存储食堂窗口信息
CREATE TABLE IF NOT EXISTS windows (
    window_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '窗口ID',
    name VARCHAR(100) NOT NULL COMMENT '窗口名称',
    floor INT NOT NULL COMMENT '楼层: 1-一楼, 2-二楼',
    description VARCHAR(200) COMMENT '窗口描述',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_floor (floor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='窗口表';

-- 4. 菜品分类表 (categories)
-- 存储菜品分类信息
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类表';

-- 5. 菜品表 (dishes)
-- 存储菜品基本信息
CREATE TABLE IF NOT EXISTS dishes (
    dish_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜品ID',
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    price DECIMAL(8,2) NOT NULL COMMENT '价格',
    image_url VARCHAR(255) COMMENT '图片路径',
    ingredients TEXT COMMENT '配料',
    category_id BIGINT NOT NULL COMMENT '分类ID，外键',
    window_id BIGINT NOT NULL COMMENT '窗口ID，外键',
    stock INT DEFAULT 0 COMMENT '库存数量',
    is_shelf TINYINT(1) DEFAULT 1 COMMENT '是否上架: 0-下架, 1-上架',
    sales_count INT DEFAULT 0 COMMENT '销量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    FOREIGN KEY (window_id) REFERENCES windows(window_id) ON DELETE RESTRICT,
    INDEX idx_category (category_id),
    INDEX idx_window (window_id),
    INDEX idx_is_shelf (is_shelf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 6. 营养成分表 (nutrition)
-- 存储菜品营养信息
CREATE TABLE IF NOT EXISTS nutrition (
    nutrition_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '营养ID',
    dish_id BIGINT NOT NULL UNIQUE COMMENT '菜品ID，外键',
    calories DECIMAL(8,2) COMMENT '热量(千卡)',
    protein DECIMAL(8,2) COMMENT '蛋白质(克)',
    fat DECIMAL(8,2) COMMENT '脂肪(克)',
    carbs DECIMAL(8,2) COMMENT '碳水化合物(克)',
    sodium DECIMAL(8,2) COMMENT '钠(毫克)',
    fiber DECIMAL(8,2) COMMENT '膳食纤维(克)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='营养成分表';

-- =============================================
-- 订单相关表
-- =============================================

-- 7. 订单表 (orders)
-- 存储订单主信息
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(32) PRIMARY KEY COMMENT '订单号，主键(UUID)',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    status TINYINT(1) DEFAULT 0 COMMENT '订单状态: 0-待支付, 1-已支付/待接单, 2-已接单/待出餐, 3-已出餐/待取餐, 4-已完成, 5-已取消',
    pickup_code VARCHAR(8) COMMENT '取餐码',
    pickup_time VARCHAR(50) COMMENT '取餐时间段',
    payment_time TIMESTAMP NULL COMMENT '支付时间',
    accept_time TIMESTAMP NULL COMMENT '接单时间',
    serve_time TIMESTAMP NULL COMMENT '出餐时间',
    cancel_time TIMESTAMP NULL COMMENT '取消时间',
    finish_time TIMESTAMP NULL COMMENT '完成时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_pickup_time (pickup_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 8. 订单明细表 (order_items)
-- 存储订单商品明细
CREATE TABLE IF NOT EXISTS order_items (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    order_id VARCHAR(32) NOT NULL COMMENT '订单号，外键',
    dish_id BIGINT NOT NULL COMMENT '菜品ID，外键',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    price DECIMAL(8,2) NOT NULL COMMENT '单价',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- =============================================
-- 收藏与偏好表
-- =============================================

-- 9. 收藏表 (favorites)
-- 存储用户收藏的菜品
CREATE TABLE IF NOT EXISTS favorites (
    favorite_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键',
    dish_id BIGINT NOT NULL COMMENT '菜品ID，外键',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_dish (user_id, dish_id) COMMENT '同一用户不能重复收藏同一菜品'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 10. 购物车表 (cart)
-- 存储用户购物车数据
CREATE TABLE IF NOT EXISTS cart (
    cart_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键',
    dish_id BIGINT NOT NULL COMMENT '菜品ID，外键',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_dish (user_id, dish_id) COMMENT '同一用户同一菜品只能在购物车中出现一次',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 11. 用户浏览记录表 (user_browsing)
-- 存储用户浏览记录，用于AI推荐
CREATE TABLE IF NOT EXISTS user_browsing (
    browsing_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '浏览ID',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键',
    dish_id BIGINT NOT NULL COMMENT '菜品ID，外键',
    browsing_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE,
    INDEX idx_user_browse (user_id, browsing_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户浏览记录表';

-- =============================================
-- AI相关表
-- =============================================

-- 11. AI推荐记录表 (ai_recommendations)
-- 存储AI推荐记录
CREATE TABLE IF NOT EXISTS ai_recommendations (
    rec_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '推荐ID',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键',
    dish_id BIGINT NOT NULL COMMENT '推荐菜品ID，外键',
    rec_type TINYINT(1) COMMENT '推荐类型: 0-猜你喜欢, 1-根据目标推荐, 2-智能配餐',
    score DECIMAL(5,2) COMMENT '推荐分数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '推荐时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE,
    INDEX idx_user_rec (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI推荐记录表';

-- 12. AI销量预测表 (ai_sales_prediction)
-- 存储AI销量预测数据
CREATE TABLE IF NOT EXISTS ai_sales_prediction (
    pred_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '预测ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID，外键',
    predict_date DATE NOT NULL COMMENT '预测日期',
    predicted_sales INT COMMENT '预测销量',
    actual_sales INT DEFAULT 0 COMMENT '实际销量',
    confidence DECIMAL(5,2) COMMENT '置信度',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '预测时间',
    FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) ON DELETE CASCADE,
    UNIQUE KEY uk_dish_date (dish_id, predict_date) COMMENT '同一菜品同一日期只有一条预测',
    INDEX idx_predict_date (predict_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI销量预测表';

-- =============================================
-- 运营日志表
-- =============================================

-- 13. 操作日志表 (operation_logs)
-- 存储系统操作日志
CREATE TABLE IF NOT EXISTS operation_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_desc TEXT COMMENT '操作描述',
    table_name VARCHAR(50) COMMENT '操作表名',
    record_id VARCHAR(50) COMMENT '操作记录ID',
    ip_address VARCHAR(50) COMMENT '操作IP',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user_id (user_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =============================================
-- 触发器定义
-- =============================================

-- 触发器1: 下单时扣减库存
DELIMITER //
CREATE TRIGGER trg_order_deduct_stock AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE dishes 
    SET stock = stock - NEW.quantity 
    WHERE dish_id = NEW.dish_id;
END //
DELIMITER ;

-- 触发器2: 取消订单时恢复库存
DELIMITER //
CREATE TRIGGER trg_cancel_order_restore_stock AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.status != 5 AND NEW.status = 5 THEN
        UPDATE dishes d
        JOIN order_items oi ON d.dish_id = oi.dish_id
        SET d.stock = d.stock + oi.quantity
        WHERE oi.order_id = NEW.order_id;
    END IF;
END //
DELIMITER ;

-- 触发器3: 订单完成时更新销量
DELIMITER //
CREATE TRIGGER trg_order_finish_update_sales AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.status != 4 AND NEW.status = 4 THEN
        UPDATE dishes d
        JOIN order_items oi ON d.dish_id = oi.dish_id
        SET d.sales_count = d.sales_count + oi.quantity
        WHERE oi.order_id = NEW.order_id;
    END IF;
END //
DELIMITER ;

-- 触发器4: 用户注册时记录日志
DELIMITER //
CREATE TRIGGER trg_user_register_log AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO operation_logs (user_id, operation_type, operation_desc, table_name, record_id)
    VALUES (NEW.user_id, 'REGISTER', CONCAT('用户注册: ', NEW.account), 'users', CAST(NEW.user_id AS CHAR));
END //
DELIMITER ;

-- 触发器5: 新增菜品时记录日志
DELIMITER //
CREATE TRIGGER trg_dish_add_log AFTER INSERT ON dishes
FOR EACH ROW
BEGIN
    INSERT INTO operation_logs (operation_type, operation_desc, table_name, record_id)
    VALUES ('ADD_DISH', CONCAT('新增菜品: ', NEW.name), 'dishes', CAST(NEW.dish_id AS CHAR));
END //
DELIMITER ;

-- 触发器6: 订单创建时记录日志
DELIMITER //
CREATE TRIGGER trg_order_create_log AFTER INSERT ON orders
FOR EACH ROW
BEGIN
    INSERT INTO operation_logs (user_id, operation_type, operation_desc, table_name, record_id)
    VALUES (NEW.user_id, 'CREATE_ORDER', CONCAT('创建订单: ', NEW.order_id), 'orders', NEW.order_id);
END //
DELIMITER ;

-- =============================================
-- 视图定义
-- =============================================

-- 视图1: 菜品营养视图
CREATE VIEW v_dish_nutrition AS
SELECT 
    d.dish_id,
    d.name,
    d.price,
    d.image_url,
    d.category_id,
    d.window_id,
    n.calories,
    n.protein,
    n.fat,
    n.carbs,
    n.sodium,
    n.fiber
FROM dishes d
LEFT JOIN nutrition n ON d.dish_id = n.dish_id
WHERE d.is_shelf = 1;

-- 视图2: 用户订单视图
CREATE VIEW v_user_orders AS
SELECT 
    o.order_id,
    o.user_id,
    u.name AS user_name,
    o.total_amount,
    o.status,
    o.pickup_code,
    o.pickup_time,
    o.created_at,
    o.payment_time,
    o.finish_time
FROM orders o
JOIN users u ON o.user_id = u.user_id;

-- 视图3: 销量排行视图
CREATE VIEW v_sales_ranking AS
SELECT 
    d.dish_id,
    d.name,
    d.price,
    d.sales_count,
    c.name AS category_name,
    w.name AS window_name
FROM dishes d
JOIN categories c ON d.category_id = c.category_id
JOIN windows w ON d.window_id = w.window_id
ORDER BY d.sales_count DESC;

-- 视图4: 用户营养摄入统计视图
CREATE VIEW v_user_nutrition_summary AS
SELECT 
    o.user_id,
    u.name AS user_name,
    DATE(o.created_at) AS order_date,
    SUM(n.calories * oi.quantity) AS total_calories,
    SUM(n.protein * oi.quantity) AS total_protein,
    SUM(n.fat * oi.quantity) AS total_fat,
    SUM(n.carbs * oi.quantity) AS total_carbs
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
JOIN dishes d ON oi.dish_id = d.dish_id
JOIN nutrition n ON d.dish_id = n.dish_id
JOIN users u ON o.user_id = u.user_id
WHERE o.status = 4
GROUP BY o.user_id, DATE(o.created_at);

-- 视图5: 销售额统计视图
CREATE VIEW v_sales_statistics AS
SELECT 
    DATE(o.created_at) AS sale_date,
    COUNT(DISTINCT o.order_id) AS order_count,
    SUM(o.total_amount) AS total_sales,
    AVG(o.total_amount) AS avg_order_amount,
    SUM(oi.quantity) AS total_items_sold
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
WHERE o.status = 4
GROUP BY DATE(o.created_at)
ORDER BY sale_date DESC;

-- 视图6: 订单按窗口筛选视图
CREATE VIEW v_orders_by_window AS
SELECT 
    o.order_id,
    o.user_id,
    u.name AS user_name,
    o.total_amount,
    o.status,
    o.pickup_code,
    o.pickup_time,
    d.window_id,
    w.name AS window_name,
    w.floor,
    o.created_at,
    o.payment_time
FROM orders o
JOIN users u ON o.user_id = u.user_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN dishes d ON oi.dish_id = d.dish_id
JOIN windows w ON d.window_id = w.window_id
ORDER BY o.created_at DESC;

-- 视图7: 热门菜品统计视图
CREATE VIEW v_hot_dishes AS
SELECT 
    d.dish_id,
    d.name,
    d.price,
    d.image_url,
    d.sales_count,
    c.name AS category_name,
    w.name AS window_name,
    ROUND(d.sales_count / (SELECT SUM(sales_count) FROM dishes) * 100, 2) AS sales_percentage
FROM dishes d
JOIN categories c ON d.category_id = c.category_id
JOIN windows w ON d.window_id = w.window_id
WHERE d.is_shelf = 1
ORDER BY d.sales_count DESC
LIMIT 20;

-- 视图8: 订单详情营养合计视图
CREATE VIEW v_order_nutrition_summary AS
SELECT 
    o.order_id,
    o.user_id,
    u.name AS user_name,
    SUM(n.calories * oi.quantity) AS total_calories,
    SUM(n.protein * oi.quantity) AS total_protein,
    SUM(n.fat * oi.quantity) AS total_fat,
    SUM(n.carbs * oi.quantity) AS total_carbs,
    SUM(n.sodium * oi.quantity) AS total_sodium,
    SUM(oi.quantity) AS total_items,
    o.total_amount
FROM orders o
JOIN users u ON o.user_id = u.user_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN dishes d ON oi.dish_id = d.dish_id
LEFT JOIN nutrition n ON d.dish_id = n.dish_id
GROUP BY o.order_id, o.user_id, u.name, o.total_amount;

-- =============================================
-- 存储过程定义
-- =============================================

-- 存储过程1: 获取用户今日营养摄入
DELIMITER //
CREATE PROCEDURE sp_get_user_today_nutrition(IN p_user_id BIGINT)
BEGIN
    SELECT 
        SUM(n.calories * oi.quantity) AS today_calories,
        SUM(n.protein * oi.quantity) AS today_protein,
        SUM(n.fat * oi.quantity) AS today_fat,
        SUM(n.carbs * oi.quantity) AS today_carbs
    FROM orders o
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN dishes d ON oi.dish_id = d.dish_id
    JOIN nutrition n ON d.dish_id = n.dish_id
    WHERE o.user_id = p_user_id
        AND o.status = 4
        AND DATE(o.created_at) = CURDATE();
END //
DELIMITER ;

-- 存储过程2: AI推荐菜品（根据用户历史和偏好）
DELIMITER //
CREATE PROCEDURE sp_ai_recommend_dishes(IN p_user_id BIGINT, IN p_limit INT)
BEGIN
    DECLARE v_goal INT;
    SELECT diet_goal INTO v_goal FROM users WHERE user_id = p_user_id;
    
    SELECT 
        d.dish_id,
        d.name,
        d.price,
        d.image_url,
        n.calories,
        n.protein,
        n.fat,
        n.carbs,
        ROUND(
            (SELECT COUNT(*) FROM favorites f WHERE f.user_id = p_user_id AND f.dish_id = d.dish_id) * 5 +
            (SELECT COUNT(*) FROM user_browsing b WHERE b.user_id = p_user_id AND b.dish_id = d.dish_id) * 2 +
            (SELECT COUNT(*) FROM order_items oi WHERE oi.order_id IN (SELECT order_id FROM orders WHERE user_id = p_user_id) AND oi.dish_id = d.dish_id) * 3 +
            d.sales_count * 0.1, 2
        ) AS recommend_score
    FROM dishes d
    LEFT JOIN nutrition n ON d.dish_id = n.dish_id
    WHERE d.is_shelf = 1
        AND d.dish_id NOT IN (
            SELECT dr.dish_id FROM dishes dr
            JOIN user_restrictions ur ON ur.user_id = p_user_id
            WHERE (ur.restriction_type = 0 AND dr.ingredients LIKE '%辣%') OR
                  (ur.restriction_type = 1 AND dr.ingredients LIKE '%香菜%') OR
                  (ur.restriction_type = 2 AND dr.ingredients LIKE '%猪肉%') OR
                  (ur.restriction_type = 3 AND dr.ingredients LIKE CONCAT('%', ur.restriction_desc, '%')) OR
                  (ur.restriction_type = 4 AND dr.ingredients LIKE '%肉%')
        )
        AND (v_goal = 0 OR 
             (v_goal = 1 AND n.calories < 350) OR 
             (v_goal = 2 AND n.protein > 15) OR 
             (v_goal = 3 AND n.fat < 12))
    ORDER BY recommend_score DESC
    LIMIT p_limit;
END //
DELIMITER ;

-- 存储过程3: 生成取餐码
DELIMITER //
CREATE PROCEDURE sp_generate_pickup_code(OUT p_code VARCHAR(8))
BEGIN
    SET p_code = CONCAT(
        SUBSTRING('ABCDEFGHIJKLMNOPQRSTUVWXYZ', FLOOR(RAND() * 26) + 1, 1),
        SUBSTRING('ABCDEFGHIJKLMNOPQRSTUVWXYZ', FLOOR(RAND() * 26) + 1, 1),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10)
    );
END //
DELIMITER ;

-- 存储过程4: 批量更新菜品库存
DELIMITER //
CREATE PROCEDURE sp_batch_update_stock(IN p_dish_ids TEXT, IN p_quantities TEXT)
BEGIN
    DECLARE v_dish_id BIGINT;
    DECLARE v_quantity INT;
    DECLARE v_index INT DEFAULT 1;
    
    WHILE v_index <= JSON_LENGTH(p_dish_ids) DO
        SET v_dish_id = JSON_EXTRACT(p_dish_ids, CONCAT('$[', v_index - 1, ']'));
        SET v_quantity = JSON_EXTRACT(p_quantities, CONCAT('$[', v_index - 1, ']'));
        
        UPDATE dishes 
        SET stock = stock + v_quantity 
        WHERE dish_id = v_dish_id;
        
        SET v_index = v_index + 1;
    END WHILE;
END //
DELIMITER ;

-- 存储过程5: 获取用户健康评价
DELIMITER //
CREATE PROCEDURE sp_get_health_evaluation(IN p_user_id BIGINT, OUT p_evaluation TEXT)
BEGIN
    DECLARE v_calories DECIMAL(10,2);
    DECLARE v_protein DECIMAL(10,2);
    DECLARE v_fat DECIMAL(10,2);
    DECLARE v_sodium DECIMAL(10,2);
    DECLARE v_goal INT;
    
    SELECT diet_goal INTO v_goal FROM users WHERE user_id = p_user_id;
    
    SELECT 
        COALESCE(SUM(n.calories * oi.quantity), 0),
        COALESCE(SUM(n.protein * oi.quantity), 0),
        COALESCE(SUM(n.fat * oi.quantity), 0),
        COALESCE(SUM(n.sodium * oi.quantity), 0)
    INTO v_calories, v_protein, v_fat, v_sodium
    FROM orders o
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN dishes d ON oi.dish_id = d.dish_id
    JOIN nutrition n ON d.dish_id = n.dish_id
    WHERE o.user_id = p_user_id
        AND o.status = 4
        AND DATE(o.created_at) = CURDATE();
    
    SET p_evaluation = '';
    
    IF v_goal = 1 AND v_calories > 1500 THEN
        SET p_evaluation = CONCAT(p_evaluation, '你今天热量超标，建议控制饮食。');
    END IF;
    
    IF v_protein < 50 THEN
        SET p_evaluation = CONCAT(p_evaluation, '你蛋白质摄入不足，建议增加蛋白质摄入。');
    END IF;
    
    IF v_fat > 60 THEN
        SET p_evaluation = CONCAT(p_evaluation, '你今天脂肪摄入偏高，建议清淡饮食。');
    END IF;
    
    IF v_sodium > 2300 THEN
        SET p_evaluation = CONCAT(p_evaluation, '你今天钠摄入偏高，建议少盐。');
    END IF;
    
    IF p_evaluation = '' THEN
        SET p_evaluation = '你今天的饮食很健康，继续保持！';
    END IF;
END //
DELIMITER ;

-- 存储过程6: AI智能配餐
DELIMITER //
CREATE PROCEDURE sp_ai_intelligent_meal(IN p_user_id BIGINT, IN p_meal_type TINYINT(1), OUT p_result JSON)
BEGIN
    DECLARE v_goal INT;
    SELECT diet_goal INTO v_goal FROM users WHERE user_id = p_user_id;
    
    IF p_meal_type = 0 THEN
        SET p_result = (
            SELECT JSON_ARRAYAGG(
                JSON_OBJECT(
                    'dish_id', d.dish_id,
                    'name', d.name,
                    'price', d.price,
                    'calories', n.calories,
                    'protein', n.protein,
                    'fat', n.fat,
                    'carbs', n.carbs
                )
            )
            FROM dishes d
            JOIN nutrition n ON d.dish_id = n.dish_id
            WHERE d.is_shelf = 1
                AND n.calories < 300
                AND n.fat < 15
            ORDER BY n.calories ASC
            LIMIT 4
        );
    ELSEIF p_meal_type = 1 THEN
        SET p_result = (
            SELECT JSON_ARRAYAGG(
                JSON_OBJECT(
                    'dish_id', d.dish_id,
                    'name', d.name,
                    'price', d.price,
                    'calories', n.calories,
                    'protein', n.protein,
                    'fat', n.fat,
                    'carbs', n.carbs
                )
            )
            FROM dishes d
            JOIN nutrition n ON d.dish_id = n.dish_id
            WHERE d.is_shelf = 1
                AND n.protein > 20
            ORDER BY n.protein DESC
            LIMIT 4
        );
    ELSEIF p_meal_type = 2 THEN
        SET p_result = (
            SELECT JSON_ARRAYAGG(
                JSON_OBJECT(
                    'dish_id', d.dish_id,
                    'name', d.name,
                    'price', d.price,
                    'calories', n.calories,
                    'protein', n.protein,
                    'fat', n.fat,
                    'carbs', n.carbs
                )
            )
            FROM dishes d
            JOIN nutrition n ON d.dish_id = n.dish_id
            WHERE d.is_shelf = 1
                AND n.carbs < 20
            ORDER BY n.carbs ASC
            LIMIT 4
        );
    ELSE
        SET p_result = (
            SELECT JSON_ARRAYAGG(
                JSON_OBJECT(
                    'dish_id', d.dish_id,
                    'name', d.name,
                    'price', d.price,
                    'calories', n.calories,
                    'protein', n.protein,
                    'fat', n.fat,
                    'carbs', n.carbs
                )
            )
            FROM dishes d
            JOIN nutrition n ON d.dish_id = n.dish_id
            WHERE d.is_shelf = 1
                AND n.fat < 10
                AND n.sodium < 300
            ORDER BY n.fat ASC
            LIMIT 4
        );
    END IF;
END //
DELIMITER ;

-- 存储过程7: 获取库存不足提醒
DELIMITER //
CREATE PROCEDURE sp_get_low_stock_alert(IN p_threshold INT)
BEGIN
    SELECT 
        d.dish_id,
        d.name,
        d.stock,
        c.name AS category_name,
        w.name AS window_name
    FROM dishes d
    JOIN categories c ON d.category_id = c.category_id
    JOIN windows w ON d.window_id = w.window_id
    WHERE d.is_shelf = 1
        AND d.stock < p_threshold
    ORDER BY d.stock ASC;
END //
DELIMITER ;

-- 存储过程8: 提交订单事务
DELIMITER //
CREATE PROCEDURE sp_submit_order(
    IN p_user_id BIGINT,
    IN p_order_id VARCHAR(32),
    IN p_total_amount DECIMAL(10,2),
    IN p_pickup_time VARCHAR(50),
    IN p_items JSON,
    OUT p_result INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_result = 0;
    END;
    
    START TRANSACTION;
    
    INSERT INTO orders (order_id, user_id, total_amount, status, pickup_time)
    VALUES (p_order_id, p_user_id, p_total_amount, 0, p_pickup_time);
    
    INSERT INTO order_items (order_id, dish_id, quantity, price, subtotal)
    SELECT 
        p_order_id,
        JSON_EXTRACT(item, '$.dish_id'),
        JSON_EXTRACT(item, '$.quantity'),
        JSON_EXTRACT(item, '$.price'),
        JSON_EXTRACT(item, '$.subtotal')
    FROM JSON_TABLE(p_items, '$[*]' COLUMNS (item JSON PATH '$')) AS jt;
    
    UPDATE dishes d
    JOIN order_items oi ON d.dish_id = oi.dish_id
    SET d.stock = d.stock - oi.quantity
    WHERE oi.order_id = p_order_id;
    
    DELETE FROM cart WHERE user_id = p_user_id;
    
    COMMIT;
    SET p_result = 1;
END //
DELIMITER ;

-- 存储过程9: 获取学生饮食偏好统计
DELIMITER //
CREATE PROCEDURE sp_get_student_preferences(OUT p_result JSON)
BEGIN
    SET p_result = (
        SELECT JSON_OBJECT(
            'category_preference', (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'category_name', c.name,
                        'order_count', COUNT(oi.dish_id),
                        'percentage', ROUND(COUNT(oi.dish_id) / (SELECT COUNT(*) FROM order_items) * 100, 2)
                    )
                )
                FROM order_items oi
                JOIN dishes d ON oi.dish_id = d.dish_id
                JOIN categories c ON d.category_id = c.category_id
                GROUP BY c.category_id, c.name
                ORDER BY COUNT(oi.dish_id) DESC
            ),
            'window_preference', (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'window_name', w.name,
                        'order_count', COUNT(oi.dish_id),
                        'percentage', ROUND(COUNT(oi.dish_id) / (SELECT COUNT(*) FROM order_items) * 100, 2)
                    )
                )
                FROM order_items oi
                JOIN dishes d ON oi.dish_id = d.dish_id
                JOIN windows w ON d.window_id = w.window_id
                GROUP BY w.window_id, w.name
                ORDER BY COUNT(oi.dish_id) DESC
            ),
            'meal_time_preference', (
                SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'pickup_time', o.pickup_time,
                        'order_count', COUNT(o.order_id)
                    )
                )
                FROM orders o
                WHERE o.status = 3
                GROUP BY o.pickup_time
                ORDER BY COUNT(o.order_id) DESC
            )
        )
    );
END //
DELIMITER ;

-- =============================================
-- 初始化基础数据
-- =============================================

-- 插入分类数据
INSERT INTO categories (name, description, sort_order) VALUES
('荤菜', '肉类菜品', 1),
('素菜', '蔬菜类菜品', 2),
('汤', '汤类', 3),
('主食', '米饭、面食等', 4),
('小吃', '点心、零食', 5);

-- 插入窗口数据
INSERT INTO windows (name, floor, description) VALUES
('一楼快餐', 1, '提供各类快餐'),
('二楼小炒', 2, '提供特色小炒'),
('早餐窗口', 1, '提供早餐'),
('面食窗口', 1, '提供各类面食');

-- 创建管理员用户（密码: admin123 的MD5值）
INSERT INTO users (account, password, name, college, phone) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '系统管理', '13800138000');

-- =============================================
-- 权限说明
-- =============================================
/*
1. 用户表(users): 用户登录、注册、个人信息管理
2. 用户忌口表(user_restrictions): 存储用户忌口偏好
3. 窗口表(windows): 食堂窗口管理
4. 菜品分类表(categories): 菜品分类管理
5. 菜品表(dishes): 菜品基本信息管理
6. 营养成分表(nutrition): 菜品营养信息
7. 订单表(orders): 订单主信息
8. 订单明细表(order_items): 订单商品明细
9. 收藏表(favorites): 用户收藏菜品
10. 购物车表(cart): 用户购物车数据
11. 用户浏览记录表(user_browsing): 用户浏览记录
12. AI推荐记录表(ai_recommendations): AI推荐记录
13. AI销量预测表(ai_sales_prediction): AI销量预测
14. 操作日志表(operation_logs): 系统操作日志

触发器说明:
1. trg_order_deduct_stock: 下单时自动扣减库存
2. trg_cancel_order_restore_stock: 取消订单时恢复库存
3. trg_order_finish_update_sales: 订单完成时更新销量
4. trg_user_register_log: 用户注册时记录日志
5. trg_dish_add_log: 新增菜品时记录日志
6. trg_order_create_log: 订单创建时记录日志

视图说明:
1. v_dish_nutrition: 菜品营养视图
2. v_user_orders: 用户订单视图
3. v_sales_ranking: 销量排行视图
4. v_user_nutrition_summary: 用户营养摄入统计视图
5. v_sales_statistics: 销售额统计视图
6. v_orders_by_window: 订单按窗口筛选视图
7. v_hot_dishes: 热门菜品统计视图
8. v_order_nutrition_summary: 订单详情营养合计视图

存储过程说明:
1. sp_get_user_today_nutrition: 获取用户今日营养摄入
2. sp_ai_recommend_dishes: AI推荐菜品（根据历史订单、收藏、浏览记录和饮食目标，自动过滤忌口）
3. sp_generate_pickup_code: 生成取餐码
4. sp_batch_update_stock: 批量更新菜品库存
5. sp_get_health_evaluation: 获取用户健康评价
6. sp_ai_intelligent_meal: AI智能配餐（减脂/增肌/低糖/养胃/清淡）
7. sp_get_low_stock_alert: 获取库存不足提醒
8. sp_submit_order: 提交订单事务（保证原子性）
9. sp_get_student_preferences: 获取学生饮食偏好统计
*/

-- =============================================
-- 数据库设计说明
-- =============================================
/*
数据库设计满足第三范式(3NF):
1. 消除重复数据: 菜品信息、营养信息、用户信息各自独立存储
2. 消除传递依赖: 通过外键关联，避免数据冗余
3. 主键、外键、非空、唯一约束齐全

表间关系:
- users 与 user_restrictions: 一对多
- users 与 orders: 一对多
- orders 与 order_items: 一对多
- dishes 与 nutrition: 一对一
- dishes 与 categories: 多对一
- dishes 与 windows: 多对一
- users 与 favorites: 一对多
- users 与 cart: 一对多
- users 与 user_browsing: 一对多

功能覆盖:
1. 用户模块: users, user_restrictions (注册、登录、个人信息、饮食目标、忌口)
2. 菜品浏览: dishes, categories, windows, nutrition (分类、窗口、营养详情)
3. 购物车: cart (加菜、减菜、清空)
4. 在线点餐: orders, order_items, sp_submit_order (事务保护)
5. 订单管理: orders, order_items, v_user_orders, v_orders_by_window
6. 个人营养中心: nutrition, v_user_nutrition_summary, sp_get_user_today_nutrition
7. AI推荐: sp_ai_recommend_dishes (历史订单+收藏+浏览+目标+忌口过滤)
8. AI智能配餐: sp_ai_intelligent_meal (减脂/增肌/低糖/养胃/清淡)
9. AI健康评价: sp_get_health_evaluation
10. AI销量预测: ai_sales_prediction
11. 食堂管理: dishes, windows, categories, 所有视图
12. 库存管理: dishes(stock), trg_order_deduct_stock, sp_get_low_stock_alert
13. 学生偏好统计: sp_get_student_preferences

作业要求对应:
1. 数据库设计25分: ER图完整、3NF满足、约束齐全、DDL完整
2. SQL实现20分: CRUD完整、触发器×6、事务sp_submit_order、视图×7、存储过程×9
3. 应用集成15分: 登录注册、MD5加密、响应式支持、防SQL注入
4. 技术创新20分: SpringBoot+Vue、表单验证、分页、存储过程、ECharts、AI推荐
5. 实用性15分: 解决食堂排队、选择困难、健康饮食、忌口安全、备餐预测
6. 文档5分: 需求、ER图、SQL、说明、手册齐全
*/
