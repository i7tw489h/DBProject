-- 健康评级存储函数
-- 在执行前请先在 Navicat 中双击选中 campus_canteen 数据库
USE campus_canteen;

-- 1. 创建健康评级函数（根据营养成分计算菜品健康评级）
DROP FUNCTION IF EXISTS fn_calc_health_rating;
DELIMITER //
CREATE FUNCTION fn_calc_health_rating(
    p_calories DECIMAL(10,2),
    p_protein DECIMAL(10,2),
    p_fat DECIMAL(10,2),
    p_sodium DECIMAL(10,2)
) RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE v_score INT DEFAULT 0;
    DECLARE v_rating VARCHAR(20) DEFAULT '推荐值';

    -- 热量评分 (满分30): 越低越好
    IF p_calories IS NULL THEN
        SET v_score = v_score + 12;
    ELSEIF p_calories <= 350 THEN
        SET v_score = v_score + 30;
    ELSEIF p_calories <= 500 THEN
        SET v_score = v_score + 22;
    ELSEIF p_calories <= 700 THEN
        SET v_score = v_score + 15;
    ELSE
        SET v_score = v_score + 8;
    END IF;

    -- 蛋白质评分 (满分30): 越高越好
    IF p_protein IS NULL THEN
        SET v_score = v_score + 8;
    ELSEIF p_protein >= 20 THEN
        SET v_score = v_score + 30;
    ELSEIF p_protein >= 15 THEN
        SET v_score = v_score + 25;
    ELSEIF p_protein >= 10 THEN
        SET v_score = v_score + 18;
    ELSEIF p_protein >= 5 THEN
        SET v_score = v_score + 12;
    ELSEIF p_protein > 0 THEN
        SET v_score = v_score + 6;
    ELSE
        SET v_score = v_score + 4;
    END IF;

    -- 脂肪评分 (满分20): 越低越好
    IF p_fat IS NULL THEN
        SET v_score = v_score + 10;
    ELSEIF p_fat <= 8 THEN
        SET v_score = v_score + 20;
    ELSEIF p_fat <= 15 THEN
        SET v_score = v_score + 15;
    ELSEIF p_fat <= 25 THEN
        SET v_score = v_score + 8;
    ELSE
        SET v_score = v_score + 3;
    END IF;

    -- 钠评分 (满分20): 越低越好
    IF p_sodium IS NULL THEN
        SET v_score = v_score + 8;
    ELSEIF p_sodium <= 400 THEN
        SET v_score = v_score + 20;
    ELSEIF p_sodium <= 800 THEN
        SET v_score = v_score + 14;
    ELSEIF p_sodium <= 1200 THEN
        SET v_score = v_score + 8;
    ELSE
        SET v_score = v_score + 3;
    END IF;

    -- 综合评级
    IF v_score >= 80 THEN
        SET v_rating = '优秀';
    ELSEIF v_score >= 60 THEN
        SET v_rating = '良好';
    ELSE
        SET v_rating = '推荐值';
    END IF;

    RETURN v_rating;
END //
DELIMITER ;

-- 2. 创建推荐度评分函数（用于猜你喜欢组，返回0-30的数值）
DROP FUNCTION IF EXISTS fn_calc_recommend_score;
DELIMITER //
CREATE FUNCTION fn_calc_recommend_score(
    p_calories DECIMAL(10,2),
    p_protein DECIMAL(10,2),
    p_fat DECIMAL(10,2),
    p_sodium DECIMAL(10,2)
) RETURNS DECIMAL(5,2)
DETERMINISTIC
BEGIN
    DECLARE v_score DECIMAL(5,2) DEFAULT 0.0;

    -- 热量评分 (满分10)
    IF p_calories IS NULL THEN
        SET v_score = v_score + 4;
    ELSEIF p_calories <= 350 THEN
        SET v_score = v_score + 10;
    ELSEIF p_calories <= 500 THEN
        SET v_score = v_score + 7;
    ELSEIF p_calories <= 700 THEN
        SET v_score = v_score + 5;
    ELSE
        SET v_score = v_score + 2;
    END IF;

    -- 蛋白质评分 (满分10)
    IF p_protein IS NULL THEN
        SET v_score = v_score + 2;
    ELSEIF p_protein >= 20 THEN
        SET v_score = v_score + 10;
    ELSEIF p_protein >= 15 THEN
        SET v_score = v_score + 8;
    ELSEIF p_protein >= 10 THEN
        SET v_score = v_score + 6;
    ELSEIF p_protein >= 5 THEN
        SET v_score = v_score + 4;
    ELSEIF p_protein > 0 THEN
        SET v_score = v_score + 2;
    ELSE
        SET v_score = v_score + 1;
    END IF;

    -- 脂肪评分 (满分5)
    IF p_fat IS NULL THEN
        SET v_score = v_score + 2.5;
    ELSEIF p_fat <= 8 THEN
        SET v_score = v_score + 5;
    ELSEIF p_fat <= 15 THEN
        SET v_score = v_score + 3.5;
    ELSEIF p_fat <= 25 THEN
        SET v_score = v_score + 2;
    ELSE
        SET v_score = v_score + 1;
    END IF;

    -- 钠评分 (满分5)
    IF p_sodium IS NULL THEN
        SET v_score = v_score + 2.5;
    ELSEIF p_sodium <= 400 THEN
        SET v_score = v_score + 5;
    ELSEIF p_sodium <= 800 THEN
        SET v_score = v_score + 3.5;
    ELSEIF p_sodium <= 1200 THEN
        SET v_score = v_score + 2;
    ELSE
        SET v_score = v_score + 1;
    END IF;

    RETURN v_score;
END //
DELIMITER ;
