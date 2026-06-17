/*
 Navicat Premium Dump SQL

 Source Server         : hwl
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : bighw

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 12/06/2026 21:23:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_recommendations
-- ----------------------------
DROP TABLE IF EXISTS `ai_recommendations`;
CREATE TABLE `ai_recommendations`  (
  `rec_id` bigint NOT NULL AUTO_INCREMENT COMMENT '推荐ID',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键',
  `dish_id` bigint NOT NULL COMMENT '推荐菜品ID，外键',
  `rec_type` tinyint(1) NULL DEFAULT NULL COMMENT '推荐类型: 0-猜你喜欢, 1-根据目标推荐, 2-智能配餐',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '推荐分数',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '推荐时间',
  PRIMARY KEY (`rec_id`) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  INDEX `idx_user_rec`(`user_id` ASC) USING BTREE,
  CONSTRAINT `ai_recommendations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ai_recommendations_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI推荐记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_recommendations
-- ----------------------------
INSERT INTO `ai_recommendations` VALUES (1, 1, 5, 1, 0.96, '2026-05-28 20:51:50');
INSERT INTO `ai_recommendations` VALUES (2, 1, 4, 1, 1.00, '2026-05-28 20:51:50');
INSERT INTO `ai_recommendations` VALUES (3, 1, 2, 1, 0.98, '2026-05-28 20:51:50');
INSERT INTO `ai_recommendations` VALUES (4, 1, 12, 1, 0.94, '2026-05-28 20:51:50');
INSERT INTO `ai_recommendations` VALUES (5, 1, 11, 1, 0.80, '2026-05-28 20:51:50');
INSERT INTO `ai_recommendations` VALUES (6, 1, 13, 1, 0.97, '2026-05-28 20:51:50');
INSERT INTO `ai_recommendations` VALUES (7, 1, 5, 1, 0.87, '2026-05-28 20:52:09');
INSERT INTO `ai_recommendations` VALUES (8, 1, 2, 1, 0.83, '2026-05-28 20:52:09');
INSERT INTO `ai_recommendations` VALUES (9, 1, 14, 1, 0.99, '2026-05-28 20:52:09');
INSERT INTO `ai_recommendations` VALUES (10, 1, 8, 1, 0.85, '2026-05-28 20:52:09');
INSERT INTO `ai_recommendations` VALUES (11, 1, 4, 1, 0.80, '2026-05-28 20:52:09');
INSERT INTO `ai_recommendations` VALUES (12, 1, 10, 1, 0.88, '2026-05-28 20:52:09');
INSERT INTO `ai_recommendations` VALUES (13, 10, 9, 2, 0.92, '2026-05-28 20:55:13');
INSERT INTO `ai_recommendations` VALUES (14, 10, 13, 2, 0.85, '2026-05-28 20:55:13');
INSERT INTO `ai_recommendations` VALUES (15, 10, 8, 2, 0.93, '2026-05-28 20:55:13');
INSERT INTO `ai_recommendations` VALUES (16, 10, 5, 2, 0.80, '2026-05-28 20:55:19');
INSERT INTO `ai_recommendations` VALUES (17, 10, 3, 2, 0.88, '2026-05-28 20:55:19');
INSERT INTO `ai_recommendations` VALUES (18, 10, 6, 2, 0.89, '2026-05-28 20:55:19');
INSERT INTO `ai_recommendations` VALUES (19, 10, 6, 2, 0.84, '2026-05-28 20:55:28');
INSERT INTO `ai_recommendations` VALUES (20, 10, 8, 2, 0.91, '2026-05-28 20:55:28');
INSERT INTO `ai_recommendations` VALUES (21, 10, 11, 2, 0.97, '2026-05-28 20:55:28');
INSERT INTO `ai_recommendations` VALUES (22, 10, 7, 2, 0.85, '2026-05-28 20:55:32');
INSERT INTO `ai_recommendations` VALUES (23, 10, 12, 2, 0.82, '2026-05-28 20:55:32');
INSERT INTO `ai_recommendations` VALUES (24, 10, 9, 2, 0.83, '2026-05-28 20:55:32');
INSERT INTO `ai_recommendations` VALUES (25, 10, 5, 2, 0.89, '2026-05-28 21:02:30');
INSERT INTO `ai_recommendations` VALUES (26, 10, 6, 2, 0.92, '2026-05-28 21:02:30');
INSERT INTO `ai_recommendations` VALUES (27, 10, 3, 2, 0.96, '2026-05-28 21:02:30');
INSERT INTO `ai_recommendations` VALUES (28, 10, 11, 2, 0.81, '2026-05-28 21:02:32');
INSERT INTO `ai_recommendations` VALUES (29, 10, 5, 2, 0.99, '2026-05-28 21:02:32');
INSERT INTO `ai_recommendations` VALUES (30, 10, 13, 2, 0.88, '2026-05-28 21:02:32');
INSERT INTO `ai_recommendations` VALUES (31, 10, 12, 2, 0.89, '2026-05-28 21:02:35');
INSERT INTO `ai_recommendations` VALUES (32, 10, 7, 2, 0.86, '2026-05-28 21:02:35');
INSERT INTO `ai_recommendations` VALUES (33, 10, 9, 2, 0.85, '2026-05-28 21:02:35');
INSERT INTO `ai_recommendations` VALUES (34, 10, 6, 2, 0.98, '2026-05-28 21:02:38');
INSERT INTO `ai_recommendations` VALUES (35, 10, 3, 2, 0.93, '2026-05-28 21:02:38');
INSERT INTO `ai_recommendations` VALUES (36, 10, 9, 2, 0.88, '2026-05-28 21:02:38');
INSERT INTO `ai_recommendations` VALUES (37, 10, 10, 2, 0.87, '2026-05-28 21:02:39');
INSERT INTO `ai_recommendations` VALUES (38, 10, 3, 2, 0.93, '2026-05-28 21:02:39');
INSERT INTO `ai_recommendations` VALUES (39, 10, 2, 2, 0.97, '2026-05-28 21:02:39');
INSERT INTO `ai_recommendations` VALUES (40, 10, 7, 2, 0.92, '2026-05-28 21:02:41');
INSERT INTO `ai_recommendations` VALUES (41, 10, 12, 2, 0.82, '2026-05-28 21:02:41');
INSERT INTO `ai_recommendations` VALUES (42, 10, 6, 2, 0.89, '2026-05-28 21:02:41');
INSERT INTO `ai_recommendations` VALUES (43, 11, 11, 2, 0.93, '2026-05-28 21:07:53');
INSERT INTO `ai_recommendations` VALUES (44, 11, 14, 2, 0.99, '2026-05-28 21:07:53');
INSERT INTO `ai_recommendations` VALUES (45, 11, 6, 2, 0.85, '2026-05-28 21:07:53');
INSERT INTO `ai_recommendations` VALUES (46, 10, 8, 2, 0.90, '2026-05-28 21:12:39');
INSERT INTO `ai_recommendations` VALUES (47, 10, 11, 2, 0.91, '2026-05-28 21:12:39');
INSERT INTO `ai_recommendations` VALUES (48, 10, 13, 2, 0.85, '2026-05-28 21:12:39');
INSERT INTO `ai_recommendations` VALUES (49, 10, 12, 2, 0.86, '2026-05-28 21:12:42');
INSERT INTO `ai_recommendations` VALUES (50, 10, 10, 2, 0.96, '2026-05-28 21:12:42');
INSERT INTO `ai_recommendations` VALUES (51, 10, 9, 2, 0.87, '2026-05-28 21:12:42');
INSERT INTO `ai_recommendations` VALUES (52, 11, 2, 2, 0.89, '2026-05-28 21:17:55');
INSERT INTO `ai_recommendations` VALUES (53, 11, 3, 2, 0.84, '2026-05-28 21:17:55');
INSERT INTO `ai_recommendations` VALUES (54, 11, 1, 2, 0.85, '2026-05-28 21:17:55');
INSERT INTO `ai_recommendations` VALUES (55, 10, 12, 2, 0.93, '2026-05-29 11:20:39');
INSERT INTO `ai_recommendations` VALUES (56, 10, 5, 2, 0.86, '2026-05-29 11:20:39');
INSERT INTO `ai_recommendations` VALUES (57, 10, 6, 2, 0.82, '2026-05-29 11:20:39');
INSERT INTO `ai_recommendations` VALUES (58, 10, 9, 2, 0.90, '2026-05-29 11:20:41');
INSERT INTO `ai_recommendations` VALUES (59, 10, 10, 2, 0.96, '2026-05-29 11:20:41');
INSERT INTO `ai_recommendations` VALUES (60, 10, 7, 2, 0.98, '2026-05-29 11:20:41');
INSERT INTO `ai_recommendations` VALUES (61, 10, 9, 2, 0.95, '2026-05-29 11:20:49');
INSERT INTO `ai_recommendations` VALUES (62, 10, 7, 2, 0.83, '2026-05-29 11:20:49');
INSERT INTO `ai_recommendations` VALUES (63, 10, 6, 2, 0.87, '2026-05-29 11:20:49');
INSERT INTO `ai_recommendations` VALUES (64, 10, 10, 2, 0.96, '2026-05-29 11:20:52');
INSERT INTO `ai_recommendations` VALUES (65, 10, 2, 2, 0.97, '2026-05-29 11:20:52');
INSERT INTO `ai_recommendations` VALUES (66, 10, 3, 2, 0.94, '2026-05-29 11:20:52');
INSERT INTO `ai_recommendations` VALUES (67, 10, 8, 2, 0.97, '2026-05-29 11:20:55');
INSERT INTO `ai_recommendations` VALUES (68, 10, 7, 2, 0.97, '2026-05-29 11:20:55');
INSERT INTO `ai_recommendations` VALUES (69, 10, 11, 2, 0.91, '2026-05-29 11:20:55');
INSERT INTO `ai_recommendations` VALUES (70, 10, 2, 2, 0.95, '2026-05-29 11:21:05');
INSERT INTO `ai_recommendations` VALUES (71, 10, 1, 2, 0.86, '2026-05-29 11:21:05');
INSERT INTO `ai_recommendations` VALUES (72, 10, 10, 2, 0.93, '2026-05-29 11:21:05');
INSERT INTO `ai_recommendations` VALUES (73, 10, 8, 2, 0.87, '2026-05-29 11:21:06');
INSERT INTO `ai_recommendations` VALUES (74, 10, 3, 2, 0.90, '2026-05-29 11:21:06');
INSERT INTO `ai_recommendations` VALUES (75, 10, 6, 2, 0.93, '2026-05-29 11:21:06');
INSERT INTO `ai_recommendations` VALUES (76, 10, 11, 2, 0.92, '2026-05-29 11:29:46');
INSERT INTO `ai_recommendations` VALUES (77, 10, 6, 2, 0.88, '2026-05-29 11:29:46');
INSERT INTO `ai_recommendations` VALUES (78, 10, 5, 2, 0.97, '2026-05-29 11:29:46');
INSERT INTO `ai_recommendations` VALUES (79, 10, 10, 2, 0.94, '2026-05-29 11:29:47');
INSERT INTO `ai_recommendations` VALUES (80, 10, 12, 2, 0.86, '2026-05-29 11:29:47');
INSERT INTO `ai_recommendations` VALUES (81, 10, 7, 2, 0.84, '2026-05-29 11:29:47');
INSERT INTO `ai_recommendations` VALUES (82, 10, 3, 2, 0.87, '2026-05-29 11:29:48');
INSERT INTO `ai_recommendations` VALUES (83, 10, 9, 2, 0.81, '2026-05-29 11:29:48');
INSERT INTO `ai_recommendations` VALUES (84, 10, 6, 2, 0.84, '2026-05-29 11:29:48');
INSERT INTO `ai_recommendations` VALUES (85, 10, 2, 2, 0.94, '2026-05-29 11:29:49');
INSERT INTO `ai_recommendations` VALUES (86, 10, 4, 2, 0.92, '2026-05-29 11:29:49');
INSERT INTO `ai_recommendations` VALUES (87, 10, 10, 2, 0.88, '2026-05-29 11:29:49');
INSERT INTO `ai_recommendations` VALUES (88, 10, 9, 2, 0.97, '2026-05-29 11:29:50');
INSERT INTO `ai_recommendations` VALUES (89, 10, 7, 2, 0.93, '2026-05-29 11:29:50');
INSERT INTO `ai_recommendations` VALUES (90, 10, 6, 2, 0.85, '2026-05-29 11:29:50');
INSERT INTO `ai_recommendations` VALUES (91, 11, 8, 2, 0.87, '2026-05-29 13:47:06');
INSERT INTO `ai_recommendations` VALUES (92, 11, 11, 2, 0.89, '2026-05-29 13:47:06');
INSERT INTO `ai_recommendations` VALUES (93, 11, 13, 2, 0.92, '2026-05-29 13:47:06');
INSERT INTO `ai_recommendations` VALUES (94, 11, 7, 2, 0.83, '2026-05-29 13:47:19');
INSERT INTO `ai_recommendations` VALUES (95, 11, 6, 2, 0.90, '2026-05-29 13:47:19');
INSERT INTO `ai_recommendations` VALUES (96, 11, 11, 2, 0.88, '2026-05-29 13:47:19');
INSERT INTO `ai_recommendations` VALUES (97, 11, 8, 2, 0.87, '2026-05-29 13:51:53');
INSERT INTO `ai_recommendations` VALUES (98, 11, 6, 2, 0.93, '2026-05-29 13:51:53');
INSERT INTO `ai_recommendations` VALUES (99, 11, 8, 2, 0.88, '2026-05-29 13:52:06');
INSERT INTO `ai_recommendations` VALUES (100, 11, 11, 2, 0.87, '2026-05-29 13:52:06');
INSERT INTO `ai_recommendations` VALUES (101, 10, 8, 2, 0.87, '2026-05-31 11:04:47');
INSERT INTO `ai_recommendations` VALUES (102, 10, 11, 2, 0.81, '2026-05-31 11:04:47');
INSERT INTO `ai_recommendations` VALUES (103, 10, 14, 2, 0.82, '2026-05-31 11:04:48');
INSERT INTO `ai_recommendations` VALUES (104, 10, 8, 2, 0.99, '2026-05-31 11:04:50');
INSERT INTO `ai_recommendations` VALUES (105, 10, 6, 2, 0.85, '2026-05-31 11:04:50');
INSERT INTO `ai_recommendations` VALUES (106, 10, 6, 2, 0.81, '2026-05-31 11:04:52');
INSERT INTO `ai_recommendations` VALUES (107, 10, 11, 2, 0.87, '2026-05-31 11:04:52');
INSERT INTO `ai_recommendations` VALUES (108, 10, 8, 2, 0.85, '2026-05-31 11:04:54');
INSERT INTO `ai_recommendations` VALUES (109, 10, 6, 2, 0.86, '2026-05-31 11:04:54');
INSERT INTO `ai_recommendations` VALUES (110, 10, 6, 2, 0.81, '2026-05-31 11:04:56');
INSERT INTO `ai_recommendations` VALUES (111, 10, 11, 2, 0.80, '2026-05-31 11:04:56');
INSERT INTO `ai_recommendations` VALUES (112, 10, 8, 2, 0.82, '2026-05-31 11:05:00');
INSERT INTO `ai_recommendations` VALUES (113, 10, 6, 2, 0.89, '2026-05-31 11:05:00');
INSERT INTO `ai_recommendations` VALUES (114, 10, 14, 2, 0.97, '2026-05-31 11:05:01');
INSERT INTO `ai_recommendations` VALUES (115, 10, 8, 2, 0.87, '2026-05-31 11:05:01');
INSERT INTO `ai_recommendations` VALUES (116, 10, 11, 2, 0.85, '2026-05-31 11:05:01');
INSERT INTO `ai_recommendations` VALUES (117, 10, 8, 2, 0.82, '2026-05-31 11:36:55');
INSERT INTO `ai_recommendations` VALUES (118, 10, 11, 2, 0.81, '2026-05-31 11:36:55');
INSERT INTO `ai_recommendations` VALUES (119, 10, 14, 2, 0.94, '2026-05-31 11:36:57');
INSERT INTO `ai_recommendations` VALUES (120, 10, 8, 2, 0.87, '2026-05-31 11:36:59');
INSERT INTO `ai_recommendations` VALUES (121, 10, 6, 2, 0.97, '2026-05-31 11:36:59');
INSERT INTO `ai_recommendations` VALUES (122, 10, 6, 2, 0.96, '2026-05-31 11:37:02');
INSERT INTO `ai_recommendations` VALUES (123, 10, 11, 2, 0.82, '2026-05-31 11:37:02');
INSERT INTO `ai_recommendations` VALUES (124, 10, 8, 2, 0.82, '2026-05-31 11:37:03');
INSERT INTO `ai_recommendations` VALUES (125, 10, 6, 2, 0.96, '2026-05-31 11:37:03');
INSERT INTO `ai_recommendations` VALUES (126, 10, 14, 2, 0.85, '2026-05-31 11:38:10');
INSERT INTO `ai_recommendations` VALUES (127, 10, 8, 2, 0.81, '2026-05-31 11:38:10');
INSERT INTO `ai_recommendations` VALUES (128, 10, 11, 2, 0.93, '2026-05-31 11:38:10');

-- ----------------------------
-- Table structure for ai_sales_prediction
-- ----------------------------
DROP TABLE IF EXISTS `ai_sales_prediction`;
CREATE TABLE `ai_sales_prediction`  (
  `pred_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预测ID',
  `dish_id` bigint NOT NULL COMMENT '菜品ID，外键',
  `predict_date` date NOT NULL COMMENT '预测日期',
  `predicted_sales` int NULL DEFAULT NULL COMMENT '预测销量',
  `actual_sales` int NULL DEFAULT 0 COMMENT '实际销量',
  `confidence` decimal(5, 2) NULL DEFAULT NULL COMMENT '置信度',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预测时间',
  PRIMARY KEY (`pred_id`) USING BTREE,
  UNIQUE INDEX `uk_dish_date`(`dish_id` ASC, `predict_date` ASC) USING BTREE COMMENT '同一菜品同一日期只有一条预测',
  INDEX `idx_predict_date`(`predict_date` ASC) USING BTREE,
  CONSTRAINT `ai_sales_prediction_ibfk_1` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI销量预测表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_sales_prediction
-- ----------------------------
INSERT INTO `ai_sales_prediction` VALUES (1, 1, '2026-06-13', 3, 0, 0.95, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (2, 2, '2026-06-13', 1, 0, 0.91, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (3, 3, '2026-06-13', 3, 0, 0.95, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (4, 4, '2026-06-13', 0, 0, 0.84, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (5, 5, '2026-06-13', 0, 0, 0.80, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (6, 6, '2026-06-13', 0, 0, 0.92, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (7, 7, '2026-06-13', 0, 0, 0.80, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (8, 8, '2026-06-13', 1, 0, 0.95, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (9, 9, '2026-06-13', 0, 0, 0.80, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (10, 10, '2026-06-13', 0, 0, 0.80, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (11, 11, '2026-06-13', 0, 0, 0.86, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (12, 12, '2026-06-13', 0, 0, 0.84, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (13, 13, '2026-06-13', 0, 0, 0.84, '2026-06-12 00:28:01');
INSERT INTO `ai_sales_prediction` VALUES (14, 14, '2026-06-13', 0, 0, 0.88, '2026-06-12 00:28:01');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cart_id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键',
  `dish_id` bigint NOT NULL COMMENT '菜品ID，外键',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`cart_id`) USING BTREE,
  UNIQUE INDEX `uk_user_dish`(`user_id` ASC, `dish_id` ASC) USING BTREE COMMENT '同一用户同一菜品只能在购物车中出现一次',
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1, 10, 5, 2, '2026-05-28 20:55:22', '2026-05-28 20:55:22');
INSERT INTO `cart` VALUES (2, 10, 11, 1, '2026-05-28 20:55:31', '2026-05-28 20:55:31');
INSERT INTO `cart` VALUES (3, 10, 12, 1, '2026-05-28 20:55:34', '2026-05-28 20:55:34');

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序序号',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  INDEX `idx_sort`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, '荤菜', '肉类菜品', 1, '2026-05-15 21:03:26');
INSERT INTO `categories` VALUES (2, '素菜', '蔬菜类菜品', 2, '2026-05-15 21:03:26');
INSERT INTO `categories` VALUES (3, '汤', '汤类', 3, '2026-05-15 21:03:26');
INSERT INTO `categories` VALUES (4, '主食', '米饭、面食等', 4, '2026-05-15 21:03:26');
INSERT INTO `categories` VALUES (5, '小吃', '点心、零食', 5, '2026-05-15 21:03:26');

-- ----------------------------
-- Table structure for dishes
-- ----------------------------
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes`  (
  `dish_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜品名称',
  `price` decimal(8, 2) NOT NULL COMMENT '价格',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片路径',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ingredients` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '配料',
  `category_id` bigint NOT NULL COMMENT '分类ID，外键',
  `window_id` bigint NOT NULL COMMENT '窗口ID，外键',
  `stock` int NULL DEFAULT 0 COMMENT '库存数量',
  `is_shelf` tinyint(1) NULL DEFAULT 1 COMMENT '是否上架: 0-下架, 1-上架',
  `sales_count` int NULL DEFAULT 0 COMMENT '销量',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dish_id`) USING BTREE,
  INDEX `idx_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_window`(`window_id` ASC) USING BTREE,
  INDEX `idx_is_shelf`(`is_shelf` ASC) USING BTREE,
  CONSTRAINT `dishes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `dishes_ibfk_2` FOREIGN KEY (`window_id`) REFERENCES `windows` (`window_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dishes
-- ----------------------------
INSERT INTO `dishes` VALUES (1, '宫保鸡丁', 18.00, '/images/dishes/1.jpg', '经典川菜，鸡肉嫩滑，花生酥脆，微辣酸甜', '鸡肉、花生、干辣椒、黄瓜、胡萝卜', 1, 1, 39, 1, 7, '2026-05-15 23:44:52', '2026-05-31 13:03:50');
INSERT INTO `dishes` VALUES (2, '鱼香肉丝', 16.00, '/images/dishes/2.jpg', '川味经典，肉丝鲜嫩，酸甜微辣', '猪肉丝、木耳、胡萝卜、青椒、笋丝', 1, 1, 32, 1, 3, '2026-05-15 23:45:14', '2026-06-12 00:29:31');
INSERT INTO `dishes` VALUES (3, '红烧肉', 22.00, '/images/dishes/3.jpg', '色泽红亮，肥而不腻，入口即化', '五花肉、生姜、大葱、八角、桂皮', 1, 1, 18, 1, 6, '2026-05-15 23:45:14', '2026-06-12 00:12:24');
INSERT INTO `dishes` VALUES (4, '糖醋里脊', 20.00, '/images/dishes/4.jpg', '外酥里嫩，酸甜可口', '猪里脊肉、淀粉、番茄酱、白糖', 1, 1, 32, 1, 2, '2026-05-15 23:45:14', '2026-05-26 11:12:48');
INSERT INTO `dishes` VALUES (5, '清炒西兰花', 12.00, '/images/dishes/清炒西蓝花.jpg', '清淡爽口，营养丰富', '西兰花、大蒜、盐、蚝油', 2, 1, 50, 1, 0, '2026-05-15 23:45:14', '2026-05-18 00:02:22');
INSERT INTO `dishes` VALUES (6, '蒜蓉菠菜', 10.00, '/images/dishes/蒜蓉菠菜.jpg', '蒜香浓郁，清爽开胃', '菠菜、大蒜、香油、盐', 2, 1, 45, 1, 4, '2026-05-15 23:45:14', '2026-05-31 12:59:11');
INSERT INTO `dishes` VALUES (7, '麻婆豆腐', 14.00, '/images/dishes/麻婆豆腐.jpg', '麻辣鲜香，豆腐嫩滑', '嫩豆腐、牛肉末、豆瓣酱、花椒', 2, 2, 44, 1, 0, '2026-05-15 23:45:14', '2026-05-17 23:49:24');
INSERT INTO `dishes` VALUES (8, '番茄炒蛋', 12.00, '/images/dishes/番茄炒蛋.jpg', '酸甜可口，老少皆宜', '番茄、鸡蛋、葱花、白糖', 2, 2, 43, 1, 5, '2026-05-15 23:45:14', '2026-06-05 10:59:30');
INSERT INTO `dishes` VALUES (9, '酸辣汤', 8.00, '/images/dishes/酸辣汤.jpg', '酸辣开胃，暖身暖心', '豆腐、木耳、鸡蛋、酸辣调料', 3, 3, 60, 1, 0, '2026-05-15 23:45:14', '2026-05-17 23:49:24');
INSERT INTO `dishes` VALUES (10, '排骨汤', 15.00, '/images/dishes/排骨汤.jpg', '汤鲜味美，营养滋补', '排骨、玉米、胡萝卜、生姜', 3, 3, 40, 1, 0, '2026-05-15 23:45:14', '2026-05-17 23:49:24');
INSERT INTO `dishes` VALUES (11, '白米饭', 2.00, '/images/dishes/白米饭.jpg', '粒粒分明，口感软糯', '大米', 4, 4, 198, 1, 2, '2026-05-15 23:45:14', '2026-05-26 11:15:29');
INSERT INTO `dishes` VALUES (12, '小米粥', 3.00, '/images/dishes/小米粥.jpg', '营养丰富，易于消化', '小米、水', 4, 4, 148, 1, 0, '2026-05-15 23:45:14', '2026-05-18 12:40:14');
INSERT INTO `dishes` VALUES (13, '豆沙包', 4.00, '/images/dishes/豆沙包.jpg', '香甜软糯，豆沙细腻', '面粉、红豆沙、酵母', 5, 2, 76, 1, 3, '2026-05-15 23:45:14', '2026-05-31 14:46:38');
INSERT INTO `dishes` VALUES (14, '花卷', 3.00, '/images/dishes/花卷.jpg', '层次分明，咸香可口', '面粉、葱花、盐、酵母', 5, 2, 49, 1, 10, '2026-05-15 23:45:14', '2026-06-11 23:48:43');
INSERT INTO `dishes` VALUES (15, '糖三角', 2.00, 'images\\dishes\\糖三角.png', NULL, NULL, 5, 1, 100, 1, 0, '2026-06-12 19:42:29', '2026-06-12 19:42:29');
INSERT INTO `dishes` VALUES (16, '包子', 4.00, 'https://cp1.douguo.com/upload/caiku/6/0/a/600x400_60e32582e96897b7df9608430372f22a.jpeg', NULL, NULL, 1, 1, 100, 1, 0, '2026-06-12 19:43:40', '2026-06-12 19:43:40');

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites`  (
  `favorite_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键',
  `dish_id` bigint NOT NULL COMMENT '菜品ID，外键',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`favorite_id`) USING BTREE,
  UNIQUE INDEX `uk_user_dish`(`user_id` ASC, `dish_id` ASC) USING BTREE COMMENT '同一用户不能重复收藏同一菜品',
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of favorites
-- ----------------------------

-- ----------------------------
-- Table structure for nutrition
-- ----------------------------
DROP TABLE IF EXISTS `nutrition`;
CREATE TABLE `nutrition`  (
  `nutrition_id` bigint NOT NULL AUTO_INCREMENT COMMENT '营养ID',
  `dish_id` bigint NOT NULL COMMENT '菜品ID，外键',
  `calories` decimal(8, 2) NULL DEFAULT NULL COMMENT '热量(千卡)',
  `protein` decimal(8, 2) NULL DEFAULT NULL COMMENT '蛋白质(克)',
  `fat` decimal(8, 2) NULL DEFAULT NULL COMMENT '脂肪(克)',
  `carbs` decimal(8, 2) NULL DEFAULT NULL COMMENT '碳水化合物(克)',
  `sodium` decimal(8, 2) NULL DEFAULT NULL COMMENT '钠(毫克)',
  `fiber` decimal(8, 2) NULL DEFAULT NULL COMMENT '膳食纤维(克)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`nutrition_id`) USING BTREE,
  UNIQUE INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `nutrition_ibfk_1` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '营养成分表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of nutrition
-- ----------------------------
INSERT INTO `nutrition` VALUES (2, 2, 250.00, 18.00, 14.00, 20.00, 380.00, 3.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (3, 3, 385.00, 25.00, 28.00, 8.00, 520.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (4, 4, 320.00, 16.00, 15.00, 35.00, 420.00, 2.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (5, 5, 85.00, 4.00, 3.00, 12.00, 180.00, 4.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (6, 6, 65.00, 3.00, 2.00, 8.00, 120.00, 3.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (7, 7, 180.00, 12.00, 10.00, 12.00, 650.00, 3.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (8, 8, 150.00, 10.00, 8.00, 12.00, 280.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (9, 9, 95.00, 5.00, 3.00, 12.00, 480.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (10, 10, 220.00, 18.00, 12.00, 10.00, 350.00, 0.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (11, 11, 130.00, 2.00, 0.50, 28.00, 5.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (12, 12, 100.00, 3.00, 1.00, 22.00, 150.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (13, 13, 150.00, 4.00, 8.00, 15.00, 250.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (14, 14, 180.00, 6.00, 7.00, 22.00, 320.00, 1.00, '2026-05-15 23:45:14', '2026-05-16 10:38:24');
INSERT INTO `nutrition` VALUES (20, 15, NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-12 19:42:29', '2026-06-12 19:42:29');
INSERT INTO `nutrition` VALUES (21, 16, NULL, NULL, NULL, NULL, NULL, NULL, '2026-06-12 19:43:40', '2026-06-12 19:43:40');

-- ----------------------------
-- Table structure for operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `operation_logs`;
CREATE TABLE `operation_logs`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `operation_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作描述',
  `table_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作表名',
  `record_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作记录ID',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_operation_type`(`operation_type` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operation_logs
-- ----------------------------
INSERT INTO `operation_logs` VALUES (1, 1, 'REGISTER', '用户注册: admin', 'users', '1', NULL, '2026-05-15 21:03:26');
INSERT INTO `operation_logs` VALUES (2, 8, 'REGISTER', '用户注册: test002', 'users', '8', NULL, '2026-05-15 22:50:45');
INSERT INTO `operation_logs` VALUES (3, 9, 'REGISTER', '用户注册: test003', 'users', '9', NULL, '2026-05-15 23:15:08');
INSERT INTO `operation_logs` VALUES (4, 10, 'REGISTER', '用户注册: 2430200244', 'users', '10', NULL, '2026-05-15 23:23:09');
INSERT INTO `operation_logs` VALUES (5, NULL, 'ADD_DISH', '新增菜品: ????', 'dishes', '1', NULL, '2026-05-15 23:44:52');
INSERT INTO `operation_logs` VALUES (6, NULL, 'ADD_DISH', '新增菜品: ????', 'dishes', '2', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (7, NULL, 'ADD_DISH', '新增菜品: ???', 'dishes', '3', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (8, NULL, 'ADD_DISH', '新增菜品: ????', 'dishes', '4', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (9, NULL, 'ADD_DISH', '新增菜品: ?????', 'dishes', '5', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (10, NULL, 'ADD_DISH', '新增菜品: ????', 'dishes', '6', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (11, NULL, 'ADD_DISH', '新增菜品: ????', 'dishes', '7', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (12, NULL, 'ADD_DISH', '新增菜品: ????', 'dishes', '8', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (13, NULL, 'ADD_DISH', '新增菜品: ???', 'dishes', '9', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (14, NULL, 'ADD_DISH', '新增菜品: ???', 'dishes', '10', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (15, NULL, 'ADD_DISH', '新增菜品: ??', 'dishes', '11', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (16, NULL, 'ADD_DISH', '新增菜品: ??', 'dishes', '12', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (17, NULL, 'ADD_DISH', '新增菜品: ??', 'dishes', '13', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (18, NULL, 'ADD_DISH', '新增菜品: ??', 'dishes', '14', NULL, '2026-05-15 23:45:13');
INSERT INTO `operation_logs` VALUES (19, 1, 'CREATE_ORDER', '创建订单: ORD1778903317116973', 'orders', 'ORD1778903317116973', NULL, '2026-05-16 11:48:37');
INSERT INTO `operation_logs` VALUES (20, 1, 'CREATE_ORDER', '创建订单: ORD1778903394696177', 'orders', 'ORD1778903394696177', NULL, '2026-05-16 11:49:54');
INSERT INTO `operation_logs` VALUES (21, 10, 'CREATE_ORDER', '创建订单: ORD1778905148781990', 'orders', 'ORD1778905148781990', NULL, '2026-05-16 12:19:08');
INSERT INTO `operation_logs` VALUES (22, 10, 'CREATE_ORDER', '创建订单: ORD1778905736266304', 'orders', 'ORD1778905736266304', NULL, '2026-05-16 12:28:56');
INSERT INTO `operation_logs` VALUES (23, 11, 'REGISTER', '用户注册: 2430200243', 'users', '11', NULL, '2026-05-17 14:40:25');
INSERT INTO `operation_logs` VALUES (24, 11, 'CREATE_ORDER', '创建订单: ORD177900008109320', 'orders', 'ORD177900008109320', NULL, '2026-05-17 14:41:21');
INSERT INTO `operation_logs` VALUES (25, 11, 'CREATE_ORDER', '创建订单: ORD1779000085368873', 'orders', 'ORD1779000085368873', NULL, '2026-05-17 14:41:25');
INSERT INTO `operation_logs` VALUES (26, 11, 'CREATE_ORDER', '创建订单: ORD1779000285048410', 'orders', 'ORD1779000285048410', NULL, '2026-05-17 14:44:45');
INSERT INTO `operation_logs` VALUES (27, 1, 'CREATE_ORDER', '创建订单: ORD1779001992377548', 'orders', 'ORD1779001992377548', NULL, '2026-05-17 15:13:12');
INSERT INTO `operation_logs` VALUES (28, 11, 'CREATE_ORDER', '创建订单: ORD1779005484179561', 'orders', 'ORD1779005484179561', NULL, '2026-05-17 16:11:24');
INSERT INTO `operation_logs` VALUES (29, 11, 'CREATE_ORDER', '创建订单: ORD1779027590045980', 'orders', 'ORD1779027590045980', NULL, '2026-05-17 22:19:50');
INSERT INTO `operation_logs` VALUES (30, 11, 'CREATE_ORDER', '创建订单: ORD177902825726784', 'orders', 'ORD177902825726784', NULL, '2026-05-17 22:30:57');
INSERT INTO `operation_logs` VALUES (31, 11, 'CREATE_ORDER', '创建订单: ORD1779030224063697', 'orders', 'ORD1779030224063697', NULL, '2026-05-17 23:03:44');
INSERT INTO `operation_logs` VALUES (32, 11, 'CREATE_ORDER', '创建订单: ORD1779031462782340', 'orders', 'ORD1779031462782340', NULL, '2026-05-17 23:24:22');
INSERT INTO `operation_logs` VALUES (33, 11, 'CREATE_ORDER', '创建订单: ORD1779074697226284', 'orders', 'ORD1779074697226284', NULL, '2026-05-18 11:24:57');
INSERT INTO `operation_logs` VALUES (34, 11, 'CREATE_ORDER', '创建订单: ORD1779074797474247', 'orders', 'ORD1779074797474247', NULL, '2026-05-18 11:26:37');
INSERT INTO `operation_logs` VALUES (35, 11, 'CREATE_ORDER', '创建订单: ORD1779075295875703', 'orders', 'ORD1779075295875703', NULL, '2026-05-18 11:34:55');
INSERT INTO `operation_logs` VALUES (36, 11, 'CREATE_ORDER', '创建订单: ORD1779075545077861', 'orders', 'ORD1779075545077861', NULL, '2026-05-18 11:39:05');
INSERT INTO `operation_logs` VALUES (37, 11, 'CREATE_ORDER', '创建订单: ORD17790789615939', 'orders', 'ORD17790789615939', NULL, '2026-05-18 12:36:01');
INSERT INTO `operation_logs` VALUES (38, 11, 'CREATE_ORDER', '创建订单: ORD177907921419315', 'orders', 'ORD177907921419315', NULL, '2026-05-18 12:40:14');
INSERT INTO `operation_logs` VALUES (39, 11, 'CREATE_ORDER', '创建订单: ORD1779079629041104', 'orders', 'ORD1779079629041104', NULL, '2026-05-18 12:47:09');
INSERT INTO `operation_logs` VALUES (40, 11, 'CREATE_ORDER', '创建订单: ORD1779533896583618', 'orders', 'ORD1779533896583618', NULL, '2026-05-23 18:58:16');
INSERT INTO `operation_logs` VALUES (41, 11, 'CREATE_ORDER', '创建订单: ORD1779533902644716', 'orders', 'ORD1779533902644716', NULL, '2026-05-23 18:58:22');
INSERT INTO `operation_logs` VALUES (42, 11, 'CREATE_ORDER', '创建订单: ORD1779534101466986', 'orders', 'ORD1779534101466986', NULL, '2026-05-23 19:01:41');
INSERT INTO `operation_logs` VALUES (43, 1, 'CREATE_ORDER', '创建订单: ORD1779534247763278', 'orders', 'ORD1779534247763278', NULL, '2026-05-23 19:04:07');
INSERT INTO `operation_logs` VALUES (44, 1, 'CREATE_ORDER', '创建订单: ORD1779534481681776', 'orders', 'ORD1779534481681776', NULL, '2026-05-23 19:08:01');
INSERT INTO `operation_logs` VALUES (45, 11, 'CREATE_ORDER', '创建订单: ORD1779534555844377', 'orders', 'ORD1779534555844377', NULL, '2026-05-23 19:09:15');
INSERT INTO `operation_logs` VALUES (46, 11, 'CREATE_ORDER', '创建订单: ORD1779534829818828', 'orders', 'ORD1779534829818828', NULL, '2026-05-23 19:13:49');
INSERT INTO `operation_logs` VALUES (47, 11, 'CREATE_ORDER', '创建订单: ORD17795360954766', 'orders', 'ORD17795360954766', NULL, '2026-05-23 19:34:55');
INSERT INTO `operation_logs` VALUES (48, 11, 'CREATE_ORDER', '创建订单: ORD1779538686699918', 'orders', 'ORD1779538686699918', NULL, '2026-05-23 20:18:06');
INSERT INTO `operation_logs` VALUES (49, 11, 'CREATE_ORDER', '创建订单: ORD1779539433456812', 'orders', 'ORD1779539433456812', NULL, '2026-05-23 20:30:33');
INSERT INTO `operation_logs` VALUES (50, 11, 'CREATE_ORDER', '创建订单: ORD1779539754109987', 'orders', 'ORD1779539754109987', NULL, '2026-05-23 20:35:54');
INSERT INTO `operation_logs` VALUES (51, 11, 'CREATE_ORDER', '创建订单: ORD1779539821226826', 'orders', 'ORD1779539821226826', NULL, '2026-05-23 20:37:01');
INSERT INTO `operation_logs` VALUES (52, 10, 'CREATE_ORDER', '创建订单: ORD1779697485454864', 'orders', 'ORD1779697485454864', NULL, '2026-05-25 16:24:45');
INSERT INTO `operation_logs` VALUES (53, 10, 'CREATE_ORDER', '创建订单: ORD1779764217374851', 'orders', 'ORD1779764217374851', NULL, '2026-05-26 10:56:57');
INSERT INTO `operation_logs` VALUES (54, 10, 'CREATE_ORDER', '创建订单: ORD1779765047909856', 'orders', 'ORD1779765047909856', NULL, '2026-05-26 11:10:47');
INSERT INTO `operation_logs` VALUES (55, 10, 'CREATE_ORDER', '创建订单: ORD1779765137436359', 'orders', 'ORD1779765137436359', NULL, '2026-05-26 11:12:17');
INSERT INTO `operation_logs` VALUES (56, 10, 'CREATE_ORDER', '创建订单: ORD177976520633614', 'orders', 'ORD177976520633614', NULL, '2026-05-26 11:13:26');
INSERT INTO `operation_logs` VALUES (57, 10, 'CREATE_ORDER', '创建订单: ORD1779765244031135', 'orders', 'ORD1779765244031135', NULL, '2026-05-26 11:14:04');
INSERT INTO `operation_logs` VALUES (58, 10, 'CREATE_ORDER', '创建订单: ORD177976528203740', 'orders', 'ORD177976528203740', NULL, '2026-05-26 11:14:42');
INSERT INTO `operation_logs` VALUES (59, 10, 'CREATE_ORDER', '创建订单: ORD1779972965142375', 'orders', 'ORD1779972965142375', NULL, '2026-05-28 20:56:05');
INSERT INTO `operation_logs` VALUES (60, 10, 'CREATE_ORDER', '创建订单: ORD177997392206949', 'orders', 'ORD177997392206949', NULL, '2026-05-28 21:12:02');
INSERT INTO `operation_logs` VALUES (61, 10, 'CREATE_ORDER', '创建订单: ORD1780024688617348', 'orders', 'ORD1780024688617348', NULL, '2026-05-29 11:18:08');
INSERT INTO `operation_logs` VALUES (62, 10, 'CREATE_ORDER', '创建订单: ORD1780198654537132', 'orders', 'ORD1780198654537132', NULL, '2026-05-31 11:37:34');
INSERT INTO `operation_logs` VALUES (63, 10, 'CREATE_ORDER', '创建订单: ORD1780202315026199', 'orders', 'ORD1780202315026199', NULL, '2026-05-31 12:38:35');
INSERT INTO `operation_logs` VALUES (64, 10, 'CREATE_ORDER', '创建订单: ORD1780203026159396', 'orders', 'ORD1780203026159396', NULL, '2026-05-31 12:50:26');
INSERT INTO `operation_logs` VALUES (65, 10, 'CREATE_ORDER', '创建订单: ORD1780203522388797', 'orders', 'ORD1780203522388797', NULL, '2026-05-31 12:58:42');
INSERT INTO `operation_logs` VALUES (66, 10, 'CREATE_ORDER', '创建订单: ORD1780203807428820', 'orders', 'ORD1780203807428820', NULL, '2026-05-31 13:03:27');
INSERT INTO `operation_logs` VALUES (67, 10, 'CREATE_ORDER', '创建订单: ORD1780209971679427', 'orders', 'ORD1780209971679427', NULL, '2026-05-31 14:46:11');
INSERT INTO `operation_logs` VALUES (68, 10, 'CREATE_ORDER', '创建订单: ORD1780210107996329', 'orders', 'ORD1780210107996329', NULL, '2026-05-31 14:48:28');
INSERT INTO `operation_logs` VALUES (69, 10, 'CREATE_ORDER', '创建订单: ORD1780624469752221', 'orders', 'ORD1780624469752221', NULL, '2026-06-05 09:54:29');
INSERT INTO `operation_logs` VALUES (70, 11, 'CREATE_ORDER', '创建订单: ORD1780628351620595', 'orders', 'ORD1780628351620595', NULL, '2026-06-05 10:59:11');
INSERT INTO `operation_logs` VALUES (71, NULL, 'ADD_DISH', '新增菜品: 糖三角', 'dishes', '15', NULL, '2026-06-05 11:10:26');
INSERT INTO `operation_logs` VALUES (72, NULL, 'ADD_DISH', '新增菜品: 糖三角', 'dishes', '15', NULL, '2026-06-11 23:15:45');
INSERT INTO `operation_logs` VALUES (73, NULL, 'ADD_DISH', '新增菜品: 1', 'dishes', '16', NULL, '2026-06-11 23:18:38');
INSERT INTO `operation_logs` VALUES (74, NULL, 'ADD_DISH', '新增菜品: 糖三角', 'dishes', '17', NULL, '2026-06-11 23:21:43');
INSERT INTO `operation_logs` VALUES (75, NULL, 'ADD_DISH', '新增菜品: 糖三角', 'dishes', '17', NULL, '2026-06-11 23:23:11');
INSERT INTO `operation_logs` VALUES (76, NULL, 'ADD_DISH', '新增菜品: 鸡蛋', 'dishes', '18', NULL, '2026-06-11 23:48:11');
INSERT INTO `operation_logs` VALUES (77, 10, 'CREATE_ORDER', '创建订单: ORD1781194237544907', 'orders', 'ORD1781194237544907', NULL, '2026-06-12 00:10:37');
INSERT INTO `operation_logs` VALUES (78, 10, 'CREATE_ORDER', '创建订单: ORD1781194308784536', 'orders', 'ORD1781194308784536', NULL, '2026-06-12 00:11:48');
INSERT INTO `operation_logs` VALUES (79, 10, 'CREATE_ORDER', '创建订单: ORD1781195338669918', 'orders', 'ORD1781195338669918', NULL, '2026-06-12 00:28:58');
INSERT INTO `operation_logs` VALUES (80, NULL, 'ADD_DISH', '新增菜品: 糖三角', 'dishes', '15', NULL, '2026-06-12 19:42:29');
INSERT INTO `operation_logs` VALUES (81, NULL, 'ADD_DISH', '新增菜品: 包子', 'dishes', '16', NULL, '2026-06-12 19:43:40');

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`  (
  `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号，外键',
  `dish_id` bigint NOT NULL COMMENT '菜品ID，外键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `price` decimal(8, 2) NOT NULL COMMENT '单价',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计金额',
  `window_id` bigint NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`item_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_dish_id`(`dish_id` ASC) USING BTREE,
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_items
-- ----------------------------
INSERT INTO `order_items` VALUES (13, 'ORD1779074697226284', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, 2, '2026-05-18 11:24:57');
INSERT INTO `order_items` VALUES (14, 'ORD1779074697226284', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, 1, '2026-05-18 11:24:57');
INSERT INTO `order_items` VALUES (15, 'ORD1779074697226284', 11, '白米饭', '/images/dishes/白米饭.jpg', 1, 2.00, 2.00, 4, '2026-05-18 11:24:57');
INSERT INTO `order_items` VALUES (16, 'ORD1779074797474247', 3, '红烧肉', '/images/dishes/3.jpg', 1, 22.00, 22.00, 1, '2026-05-18 11:26:37');
INSERT INTO `order_items` VALUES (17, 'ORD1779075295875703', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, 2, '2026-05-18 11:34:56');
INSERT INTO `order_items` VALUES (18, 'ORD1779075545077861', 3, '红烧肉', '/images/dishes/3.jpg', 1, 22.00, 22.00, 1, '2026-05-18 11:39:05');
INSERT INTO `order_items` VALUES (19, 'ORD1779075545077861', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, 1, '2026-05-18 11:39:05');
INSERT INTO `order_items` VALUES (20, 'ORD17790789615939', 12, '小米粥', '/images/dishes/小米粥.jpg', 1, 3.00, 3.00, 4, '2026-05-18 12:36:02');
INSERT INTO `order_items` VALUES (21, 'ORD17790789615939', 14, '花卷', '/images/dishes/花卷.jpg', 1, 3.00, 3.00, 2, '2026-05-18 12:36:02');
INSERT INTO `order_items` VALUES (22, 'ORD17790789615939', 13, '豆沙包', '/images/dishes/豆沙包.jpg', 1, 4.00, 4.00, 2, '2026-05-18 12:36:02');
INSERT INTO `order_items` VALUES (23, 'ORD177907921419315', 12, '小米粥', '/images/dishes/小米粥.jpg', 1, 3.00, 3.00, 4, '2026-05-18 12:40:14');
INSERT INTO `order_items` VALUES (24, 'ORD1779079629041104', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, 2, '2026-05-18 12:47:09');
INSERT INTO `order_items` VALUES (25, 'ORD1779534247763278', 1, NULL, NULL, 1, 18.00, 18.00, NULL, '2026-05-23 19:04:08');
INSERT INTO `order_items` VALUES (26, 'ORD1779534481681776', 3, NULL, NULL, 1, 22.00, 22.00, NULL, '2026-05-23 19:08:02');
INSERT INTO `order_items` VALUES (27, 'ORD1779534555844377', 3, NULL, NULL, 1, 22.00, 22.00, NULL, '2026-05-23 19:09:16');
INSERT INTO `order_items` VALUES (28, 'ORD1779534829818828', 3, NULL, NULL, 1, 22.00, 22.00, NULL, '2026-05-23 19:13:50');
INSERT INTO `order_items` VALUES (29, 'ORD17795360954766', 3, NULL, NULL, 1, 22.00, 22.00, NULL, '2026-05-23 19:34:55');
INSERT INTO `order_items` VALUES (30, 'ORD1779538686699918', 1, NULL, NULL, 1, 18.00, 18.00, NULL, '2026-05-23 20:18:07');
INSERT INTO `order_items` VALUES (31, 'ORD1779539433456812', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, NULL, '2026-05-23 20:30:33');
INSERT INTO `order_items` VALUES (32, 'ORD1779539754109987', 2, '鱼香肉丝', '/images/dishes/2.jpg', 1, 16.00, 16.00, NULL, '2026-05-23 20:35:54');
INSERT INTO `order_items` VALUES (33, 'ORD1779539754109987', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-23 20:35:54');
INSERT INTO `order_items` VALUES (34, 'ORD1779539821226826', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-23 20:37:01');
INSERT INTO `order_items` VALUES (35, 'ORD1779697485454864', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-25 16:24:46');
INSERT INTO `order_items` VALUES (36, 'ORD1779764217374851', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, NULL, '2026-05-26 10:56:57');
INSERT INTO `order_items` VALUES (37, 'ORD1779764217374851', 4, '糖醋里脊', '/images/dishes/4.jpg', 1, 20.00, 20.00, NULL, '2026-05-26 10:56:57');
INSERT INTO `order_items` VALUES (38, 'ORD1779764217374851', 11, '白米饭', '/images/dishes/白米饭.jpg', 1, 2.00, 2.00, NULL, '2026-05-26 10:56:57');
INSERT INTO `order_items` VALUES (39, 'ORD1779765047909856', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, NULL, '2026-05-26 11:10:48');
INSERT INTO `order_items` VALUES (40, 'ORD1779765137436359', 3, '红烧肉', '/images/dishes/3.jpg', 1, 22.00, 22.00, NULL, '2026-05-26 11:12:17');
INSERT INTO `order_items` VALUES (41, 'ORD1779765137436359', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, NULL, '2026-05-26 11:12:17');
INSERT INTO `order_items` VALUES (42, 'ORD1779765137436359', 4, '糖醋里脊', '/images/dishes/4.jpg', 1, 20.00, 20.00, NULL, '2026-05-26 11:12:17');
INSERT INTO `order_items` VALUES (43, 'ORD177976520633614', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, NULL, '2026-05-26 11:13:26');
INSERT INTO `order_items` VALUES (44, 'ORD1779765244031135', 11, '白米饭', '/images/dishes/白米饭.jpg', 1, 2.00, 2.00, NULL, '2026-05-26 11:14:04');
INSERT INTO `order_items` VALUES (45, 'ORD177976528203740', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, NULL, '2026-05-26 11:14:42');
INSERT INTO `order_items` VALUES (46, 'ORD177976528203740', 2, '鱼香肉丝', '/images/dishes/2.jpg', 1, 16.00, 16.00, NULL, '2026-05-26 11:14:42');
INSERT INTO `order_items` VALUES (47, 'ORD177976528203740', 14, '花卷', '/images/dishes/花卷.jpg', 1, 3.00, 3.00, NULL, '2026-05-26 11:14:42');
INSERT INTO `order_items` VALUES (48, 'ORD1779972965142375', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-28 20:56:05');
INSERT INTO `order_items` VALUES (49, 'ORD177997392206949', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, NULL, '2026-05-28 21:12:02');
INSERT INTO `order_items` VALUES (50, 'ORD1780024688617348', 3, '红烧肉', '/images/dishes/3.jpg', 1, 22.00, 22.00, NULL, '2026-05-29 11:18:09');
INSERT INTO `order_items` VALUES (51, 'ORD1780198654537132', 6, '蒜蓉菠菜', '/images/dishes/蒜蓉菠菜.jpg', 1, 10.00, 10.00, NULL, '2026-05-31 11:37:35');
INSERT INTO `order_items` VALUES (52, 'ORD1780202315026199', 3, '红烧肉', '/images/dishes/3.jpg', 1, 22.00, 22.00, NULL, '2026-05-31 12:38:35');
INSERT INTO `order_items` VALUES (53, 'ORD1780203026159396', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-31 12:50:26');
INSERT INTO `order_items` VALUES (54, 'ORD1780203522388797', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-31 12:58:42');
INSERT INTO `order_items` VALUES (55, 'ORD1780203807428820', 1, '宫保鸡丁', '/images/dishes/1.jpg', 1, 18.00, 18.00, NULL, '2026-05-31 13:03:27');
INSERT INTO `order_items` VALUES (56, 'ORD1780209971679427', 13, '豆沙包', '/images/dishes/豆沙包.jpg', 3, 4.00, 12.00, NULL, '2026-05-31 14:46:12');
INSERT INTO `order_items` VALUES (57, 'ORD1780210107996329', 14, '花卷', '/images/dishes/花卷.jpg', 9, 3.00, 27.00, NULL, '2026-05-31 14:48:28');
INSERT INTO `order_items` VALUES (58, 'ORD1780624469752221', 14, '花卷', '/images/dishes/花卷.jpg', 1, 3.00, 3.00, NULL, '2026-06-05 09:54:30');
INSERT INTO `order_items` VALUES (59, 'ORD1780628351620595', 8, '番茄炒蛋', '/images/dishes/番茄炒蛋.jpg', 1, 12.00, 12.00, NULL, '2026-06-05 10:59:12');
INSERT INTO `order_items` VALUES (60, 'ORD1781194237544907', 2, '鱼香肉丝', '/images/dishes/2.jpg', 1, 16.00, 16.00, NULL, '2026-06-12 00:10:38');
INSERT INTO `order_items` VALUES (61, 'ORD1781194308784536', 3, '红烧肉', '/images/dishes/3.jpg', 1, 22.00, 22.00, NULL, '2026-06-12 00:11:49');
INSERT INTO `order_items` VALUES (62, 'ORD1781195338669918', 2, '鱼香肉丝', '/images/dishes/2.jpg', 1, 16.00, 16.00, NULL, '2026-06-12 00:28:59');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号，主键(UUID)',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '订单状态: 0-待支付, 1-已支付/待接单, 2-已接单/待出餐, 3-已出餐/待取餐, 4-已完成, 5-已取消',
  `pickup_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取餐码',
  `pickup_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取餐时间段',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单备注/特殊要求',
  `payment_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `accept_time` timestamp NULL DEFAULT NULL COMMENT '接单时间',
  `serve_time` timestamp NULL DEFAULT NULL COMMENT '出餐时间',
  `cancel_time` timestamp NULL DEFAULT NULL COMMENT '取消时间',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_pickup_time`(`pickup_time` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('ORD1779074697226284', 11, 24.00, 4, '104001', '12:30-13:00', NULL, NULL, '2026-05-18 11:26:00', '2026-05-18 11:26:13', NULL, '2026-05-18 11:26:20', '2026-05-18 11:24:57', '2026-05-18 11:24:57');
INSERT INTO `orders` VALUES ('ORD1779074797474247', 11, 22.00, 1, '293313', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-18 11:26:37', '2026-05-18 11:26:37');
INSERT INTO `orders` VALUES ('ORD1779075295875703', 11, 12.00, 5, '646457', '12:00-12:30', NULL, NULL, NULL, NULL, '2026-05-18 11:35:18', NULL, '2026-05-18 11:34:56', '2026-05-18 11:34:56');
INSERT INTO `orders` VALUES ('ORD1779075545077861', 11, 32.00, 1, '894059', '12:30-13:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-18 11:39:05', '2026-05-18 11:39:05');
INSERT INTO `orders` VALUES ('ORD17790789615939', 11, 10.00, 1, '888200', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-18 12:36:02', '2026-05-18 12:36:02');
INSERT INTO `orders` VALUES ('ORD177907921419315', 11, 3.00, 2, '446349', '11:30-12:00', NULL, NULL, '2026-05-18 12:48:02', NULL, NULL, NULL, '2026-05-18 12:40:14', '2026-05-18 12:40:14');
INSERT INTO `orders` VALUES ('ORD1779079629041104', 11, 12.00, 1, '908081', '12:00-12:30', '不加葱', NULL, NULL, NULL, NULL, NULL, '2026-05-18 12:47:09', '2026-05-18 12:47:09');
INSERT INTO `orders` VALUES ('ORD1779533896583618', 11, 22.00, 1, '049940', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 18:58:17', '2026-05-23 18:58:17');
INSERT INTO `orders` VALUES ('ORD1779533902644716', 11, 22.00, 1, '197998', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 18:58:23', '2026-05-23 18:58:23');
INSERT INTO `orders` VALUES ('ORD1779534101466986', 11, 22.00, 1, '296656', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 19:01:41', '2026-05-23 19:01:41');
INSERT INTO `orders` VALUES ('ORD1779534247763278', 1, 18.00, 1, '920038', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 19:04:08', '2026-05-23 19:04:08');
INSERT INTO `orders` VALUES ('ORD1779534481681776', 1, 22.00, 1, '347635', '11:30-12:00', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 19:08:02', '2026-05-23 19:08:02');
INSERT INTO `orders` VALUES ('ORD1779534555844377', 11, 22.00, 1, '765614', '12:00-12:30', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 19:09:16', '2026-05-23 19:09:16');
INSERT INTO `orders` VALUES ('ORD1779534829818828', 11, 22.00, 2, '403486', '12:00-12:30', NULL, NULL, '2026-05-23 19:14:06', NULL, NULL, NULL, '2026-05-23 19:13:50', '2026-05-23 19:13:50');
INSERT INTO `orders` VALUES ('ORD17795360954766', 11, 22.00, 1, '961447', '12:00-12:30', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-23 19:34:55', '2026-05-23 19:34:55');
INSERT INTO `orders` VALUES ('ORD1779538686699918', 11, 18.00, 5, '269243', '11:30-12:00', '不辣', NULL, NULL, NULL, '2026-05-23 20:36:30', NULL, '2026-05-23 20:18:07', '2026-05-23 20:18:07');
INSERT INTO `orders` VALUES ('ORD1779539433456812', 11, 12.00, 3, '971143', '11:30-12:00', '不加葱', NULL, '2026-05-23 20:30:52', '2026-05-23 20:31:07', NULL, NULL, '2026-05-23 20:30:33', '2026-05-23 20:30:33');
INSERT INTO `orders` VALUES ('ORD1779539754109987', 11, 34.00, 4, '673481', '12:30-13:00', '不加葱', NULL, '2026-05-23 20:36:06', '2026-05-23 20:36:16', NULL, '2026-05-23 20:36:20', '2026-05-23 20:35:54', '2026-05-23 20:35:54');
INSERT INTO `orders` VALUES ('ORD1779539821226826', 11, 18.00, 1, '422828', '11:30-12:00', '', NULL, NULL, NULL, NULL, NULL, '2026-05-23 20:37:01', '2026-05-23 20:37:01');
INSERT INTO `orders` VALUES ('ORD1779697485454864', 10, 18.00, 4, '362757', '11:30-12:00', '', NULL, '2026-05-25 16:25:35', '2026-05-25 16:25:37', NULL, '2026-05-25 16:25:51', '2026-05-25 16:24:45', '2026-05-25 16:24:45');
INSERT INTO `orders` VALUES ('ORD1779764217374851', 10, 32.00, 4, '005945', '11:30-12:00', '', NULL, '2026-05-26 10:57:14', '2026-05-26 10:57:16', NULL, '2026-05-26 10:57:30', '2026-05-26 10:56:57', '2026-05-26 10:56:57');
INSERT INTO `orders` VALUES ('ORD1779765047909856', 10, 10.00, 4, '142207', '12:00-12:30', '', NULL, '2026-05-26 11:11:07', '2026-05-26 11:11:09', NULL, '2026-05-26 11:12:47', '2026-05-26 11:10:48', '2026-05-26 11:10:48');
INSERT INTO `orders` VALUES ('ORD1779765137436359', 10, 54.00, 4, '714152', '17:30-18:00', '', NULL, '2026-05-26 11:12:26', '2026-05-26 11:12:29', NULL, '2026-05-26 11:12:49', '2026-05-26 11:12:17', '2026-05-26 11:12:17');
INSERT INTO `orders` VALUES ('ORD177976520633614', 10, 10.00, 5, '653088', '17:30-18:00', '', NULL, NULL, NULL, '2026-05-26 11:15:32', NULL, '2026-05-26 11:13:26', '2026-05-26 11:13:26');
INSERT INTO `orders` VALUES ('ORD1779765244031135', 10, 2.00, 5, '972842', '11:30-12:00', '', NULL, NULL, NULL, '2026-05-26 11:15:29', NULL, '2026-05-26 11:14:04', '2026-05-26 11:14:04');
INSERT INTO `orders` VALUES ('ORD177976528203740', 10, 31.00, 5, '211969', '12:30-13:00', '', NULL, NULL, NULL, '2026-05-26 11:15:27', NULL, '2026-05-26 11:14:42', '2026-05-26 11:14:42');
INSERT INTO `orders` VALUES ('ORD1779972965142375', 10, 18.00, 4, '525056', '11:30-12:00', '', NULL, '2026-05-28 20:56:17', '2026-05-28 20:56:19', NULL, '2026-05-28 20:56:31', '2026-05-28 20:56:05', '2026-05-28 20:56:05');
INSERT INTO `orders` VALUES ('ORD177997392206949', 10, 12.00, 4, '904674', '11:30-12:00', '', NULL, '2026-05-28 21:12:16', '2026-05-28 21:12:17', NULL, '2026-05-28 21:12:27', '2026-05-28 21:12:02', '2026-05-28 21:12:02');
INSERT INTO `orders` VALUES ('ORD1780024688617348', 10, 22.00, 4, '628584', '11:30-12:00', '', NULL, '2026-05-29 11:18:22', '2026-05-29 11:18:23', NULL, '2026-05-31 12:59:15', '2026-05-29 11:18:09', '2026-05-29 11:18:09');
INSERT INTO `orders` VALUES ('ORD1780198654537132', 10, 10.00, 4, '437012', '11:30-12:00', '', NULL, '2026-05-31 11:37:47', '2026-05-31 11:37:48', NULL, '2026-05-31 12:59:12', '2026-05-31 11:37:35', '2026-05-31 11:37:35');
INSERT INTO `orders` VALUES ('ORD1780202315026199', 10, 22.00, 4, '572486', '11:30-12:00', '', NULL, '2026-05-31 12:38:50', '2026-05-31 12:38:53', NULL, '2026-05-31 12:59:13', '2026-05-31 12:38:35', '2026-05-31 12:38:35');
INSERT INTO `orders` VALUES ('ORD1780203026159396', 10, 18.00, 4, '304190', '11:30-12:00', '', NULL, '2026-05-31 12:50:36', '2026-05-31 12:50:37', NULL, '2026-05-31 12:59:08', '2026-05-31 12:50:26', '2026-05-31 12:50:26');
INSERT INTO `orders` VALUES ('ORD1780203522388797', 10, 18.00, 4, '102181', '11:30-12:00', '', NULL, '2026-05-31 12:58:55', '2026-05-31 12:58:57', NULL, '2026-05-31 12:59:07', '2026-05-31 12:58:42', '2026-05-31 12:58:42');
INSERT INTO `orders` VALUES ('ORD1780203807428820', 10, 18.00, 4, '033954', '11:30-12:00', '', NULL, '2026-05-31 13:03:39', '2026-05-31 13:03:42', NULL, '2026-05-31 13:03:51', '2026-05-31 13:03:27', '2026-05-31 13:03:27');
INSERT INTO `orders` VALUES ('ORD1780209971679427', 10, 12.00, 4, '385270', '12:00-12:30', '', NULL, '2026-05-31 14:46:30', '2026-05-31 14:46:31', NULL, '2026-05-31 14:46:39', '2026-05-31 14:46:12', '2026-05-31 14:46:12');
INSERT INTO `orders` VALUES ('ORD1780210107996329', 10, 27.00, 4, '171930', '12:30-13:00', '', NULL, '2026-05-31 14:48:45', '2026-05-31 14:48:47', NULL, '2026-05-31 14:51:30', '2026-05-31 14:48:28', '2026-05-31 14:48:28');
INSERT INTO `orders` VALUES ('ORD1780624469752221', 10, 3.00, 4, '558554', '12:30-13:00', '', NULL, '2026-06-05 09:54:56', '2026-06-05 09:54:57', NULL, '2026-06-05 09:55:04', '2026-06-05 09:54:30', '2026-06-05 09:54:30');
INSERT INTO `orders` VALUES ('ORD1780628351620595', 11, 12.00, 4, '196047', '11:30-12:00', '', NULL, '2026-06-05 10:59:24', '2026-06-05 10:59:25', NULL, '2026-06-05 10:59:30', '2026-06-05 10:59:12', '2026-06-05 10:59:12');
INSERT INTO `orders` VALUES ('ORD1781194237544907', 10, 16.00, 3, '813814', '11:30-12:00', '', NULL, '2026-06-12 19:46:35', '2026-06-12 19:46:36', NULL, NULL, '2026-06-12 00:10:38', '2026-06-12 00:10:38');
INSERT INTO `orders` VALUES ('ORD1781194308784536', 10, 22.00, 4, '205372', '11:30-12:00', '', NULL, '2026-06-12 00:12:10', '2026-06-12 00:12:11', NULL, '2026-06-12 00:12:24', '2026-06-12 00:11:49', '2026-06-12 00:11:49');
INSERT INTO `orders` VALUES ('ORD1781195338669918', 10, 16.00, 4, '406162', '11:30-12:00', '', NULL, '2026-06-12 00:29:23', '2026-06-12 00:29:25', NULL, '2026-06-12 00:29:32', '2026-06-12 00:28:59', '2026-06-12 00:28:59');

-- ----------------------------
-- Table structure for user_browsing
-- ----------------------------
DROP TABLE IF EXISTS `user_browsing`;
CREATE TABLE `user_browsing`  (
  `browsing_id` bigint NOT NULL AUTO_INCREMENT COMMENT '浏览ID',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键',
  `dish_id` bigint NOT NULL COMMENT '菜品ID，外键',
  `browsing_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`browsing_id`) USING BTREE,
  INDEX `dish_id`(`dish_id` ASC) USING BTREE,
  INDEX `idx_user_browse`(`user_id` ASC, `browsing_time` ASC) USING BTREE,
  CONSTRAINT `user_browsing_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_browsing_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`dish_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户浏览记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_browsing
-- ----------------------------

-- ----------------------------
-- Table structure for user_restrictions
-- ----------------------------
DROP TABLE IF EXISTS `user_restrictions`;
CREATE TABLE `user_restrictions`  (
  `restriction_id` bigint NOT NULL AUTO_INCREMENT COMMENT '忌口ID',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键',
  `restriction_type` tinyint(1) NOT NULL COMMENT '忌口类型: 0-不吃辣, 1-不吃香菜, 2-清真, 3-过敏, 4-素食',
  `restriction_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '忌口描述(如过敏食物具体名称)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`restriction_id`) USING BTREE,
  UNIQUE INDEX `uk_user_restriction`(`user_id` ASC, `restriction_type` ASC) USING BTREE COMMENT '同一用户同一忌口类型只能设置一次',
  CONSTRAINT `user_restrictions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户忌口表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_restrictions
-- ----------------------------
INSERT INTO `user_restrictions` VALUES (1, 10, 1, '花生', '2026-06-12 20:18:44');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录账号，唯一',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(MD5加密)',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `height` decimal(5, 2) NULL DEFAULT NULL COMMENT '身高(cm)',
  `weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '体重(kg)',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别: 0-女, 1-男',
  `diet_goal` tinyint(1) NULL DEFAULT NULL COMMENT '饮食目标: 0-正常, 1-减脂, 2-增肌, 3-养胃',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像路径',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_account`(`account` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '系统管理', '13800138000', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-15 21:03:26', '2026-05-15 21:03:26');
INSERT INTO `users` VALUES (8, 'test002', 'e10adc3949ba59abbe56e057f20f883e', '李四', '电子学院', '13900139000', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-15 22:50:45', '2026-05-15 22:50:45');
INSERT INTO `users` VALUES (9, 'test003', 'e10adc3949ba59abbe56e057f20f883e', '王五', NULL, '13900139003', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-15 23:15:09', '2026-05-15 23:15:09');
INSERT INTO `users` VALUES (10, '2430200244', 'f25a2fc72690b780b2a14e140ef6a9e0', 'wendy', '软件学院', '15369807341', 158.00, 48.00, 20, 2, 0, NULL, '2026-05-15 23:23:10', '2026-05-28 20:57:17');
INSERT INTO `users` VALUES (11, '2430200243', '6299b4bf69960e53b6d9a0bd27342660', 'hlz', '软件学院', '13912345678', NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-17 14:40:26', '2026-05-18 11:40:24');

-- ----------------------------
-- Table structure for windows
-- ----------------------------
DROP TABLE IF EXISTS `windows`;
CREATE TABLE `windows`  (
  `window_id` bigint NOT NULL AUTO_INCREMENT COMMENT '窗口ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '窗口名称',
  `floor` int NOT NULL COMMENT '楼层: 1-一楼, 2-二楼',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '窗口描述',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`window_id`) USING BTREE,
  INDEX `idx_floor`(`floor` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '窗口表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of windows
-- ----------------------------
INSERT INTO `windows` VALUES (1, '一楼快餐', 1, '提供各类快餐', 1, '2026-05-15 21:03:26', '2026-05-15 21:03:26');
INSERT INTO `windows` VALUES (2, '二楼小炒', 2, '提供特色小炒', 1, '2026-05-15 21:03:26', '2026-05-15 21:03:26');
INSERT INTO `windows` VALUES (3, '早餐窗口', 1, '提供早餐', 1, '2026-05-15 21:03:26', '2026-05-15 21:03:26');
INSERT INTO `windows` VALUES (4, '面食窗口', 1, '提供各类面食', 1, '2026-05-15 21:03:26', '2026-05-15 21:03:26');

-- ----------------------------
-- View structure for v_dish_nutrition
-- ----------------------------
DROP VIEW IF EXISTS `v_dish_nutrition`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_dish_nutrition` AS select `d`.`dish_id` AS `dish_id`,`d`.`name` AS `name`,`d`.`price` AS `price`,`d`.`image_url` AS `image_url`,`d`.`category_id` AS `category_id`,`d`.`window_id` AS `window_id`,`n`.`calories` AS `calories`,`n`.`protein` AS `protein`,`n`.`fat` AS `fat`,`n`.`carbs` AS `carbs`,`n`.`sodium` AS `sodium`,`n`.`fiber` AS `fiber` from (`dishes` `d` left join `nutrition` `n` on((`d`.`dish_id` = `n`.`dish_id`))) where (`d`.`is_shelf` = 1);

-- ----------------------------
-- View structure for v_hot_dishes
-- ----------------------------
DROP VIEW IF EXISTS `v_hot_dishes`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_hot_dishes` AS select `d`.`dish_id` AS `dish_id`,`d`.`name` AS `name`,`d`.`price` AS `price`,`d`.`image_url` AS `image_url`,`d`.`sales_count` AS `sales_count`,`c`.`name` AS `category_name`,`w`.`name` AS `window_name`,round(((`d`.`sales_count` / (select sum(`dishes`.`sales_count`) from `dishes`)) * 100),2) AS `sales_percentage` from ((`dishes` `d` join `categories` `c` on((`d`.`category_id` = `c`.`category_id`))) join `windows` `w` on((`d`.`window_id` = `w`.`window_id`))) where (`d`.`is_shelf` = 1) order by `d`.`sales_count` desc limit 20;

-- ----------------------------
-- View structure for v_order_detail
-- ----------------------------
DROP VIEW IF EXISTS `v_order_detail`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_order_detail` AS select `o`.`order_id` AS `order_id`,`o`.`user_id` AS `user_id`,`u`.`name` AS `user_name`,`u`.`phone` AS `phone`,`o`.`total_amount` AS `total_amount`,`o`.`status` AS `status`,`o`.`pickup_code` AS `pickup_code`,`o`.`pickup_time` AS `pickup_time`,`o`.`payment_time` AS `payment_time`,`o`.`accept_time` AS `accept_time`,`o`.`serve_time` AS `serve_time`,`o`.`cancel_time` AS `cancel_time`,`o`.`finish_time` AS `finish_time`,`o`.`created_at` AS `created_at`,`o`.`updated_at` AS `updated_at` from (`orders` `o` left join `users` `u` on((`o`.`user_id` = `u`.`user_id`)));

-- ----------------------------
-- View structure for v_order_item_detail
-- ----------------------------
DROP VIEW IF EXISTS `v_order_item_detail`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_order_item_detail` AS select `oi`.`item_id` AS `item_id`,`oi`.`order_id` AS `order_id`,`oi`.`dish_id` AS `dish_id`,`oi`.`name` AS `dish_name`,`oi`.`image_url` AS `image_url`,`oi`.`quantity` AS `quantity`,`oi`.`price` AS `price`,`oi`.`subtotal` AS `subtotal`,`oi`.`window_id` AS `window_id`,`w`.`name` AS `window_name` from (`order_items` `oi` left join `windows` `w` on((`oi`.`window_id` = `w`.`window_id`)));

-- ----------------------------
-- View structure for v_order_nutrition_summary
-- ----------------------------
DROP VIEW IF EXISTS `v_order_nutrition_summary`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_order_nutrition_summary` AS select `o`.`order_id` AS `order_id`,`o`.`user_id` AS `user_id`,`u`.`name` AS `user_name`,sum((`n`.`calories` * `oi`.`quantity`)) AS `total_calories`,sum((`n`.`protein` * `oi`.`quantity`)) AS `total_protein`,sum((`n`.`fat` * `oi`.`quantity`)) AS `total_fat`,sum((`n`.`carbs` * `oi`.`quantity`)) AS `total_carbs`,sum((`n`.`sodium` * `oi`.`quantity`)) AS `total_sodium`,sum(`oi`.`quantity`) AS `total_items`,`o`.`total_amount` AS `total_amount` from ((((`orders` `o` join `users` `u` on((`o`.`user_id` = `u`.`user_id`))) join `order_items` `oi` on((`o`.`order_id` = `oi`.`order_id`))) join `dishes` `d` on((`oi`.`dish_id` = `d`.`dish_id`))) left join `nutrition` `n` on((`d`.`dish_id` = `n`.`dish_id`))) group by `o`.`order_id`,`o`.`user_id`,`u`.`name`,`o`.`total_amount`;

-- ----------------------------
-- View structure for v_orders_by_window
-- ----------------------------
DROP VIEW IF EXISTS `v_orders_by_window`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_orders_by_window` AS select `o`.`order_id` AS `order_id`,`o`.`user_id` AS `user_id`,`u`.`name` AS `user_name`,`o`.`total_amount` AS `total_amount`,`o`.`status` AS `status`,`o`.`pickup_code` AS `pickup_code`,`o`.`pickup_time` AS `pickup_time`,`d`.`window_id` AS `window_id`,`w`.`name` AS `window_name`,`w`.`floor` AS `floor`,`o`.`created_at` AS `created_at`,`o`.`payment_time` AS `payment_time` from ((((`orders` `o` join `users` `u` on((`o`.`user_id` = `u`.`user_id`))) join `order_items` `oi` on((`o`.`order_id` = `oi`.`order_id`))) join `dishes` `d` on((`oi`.`dish_id` = `d`.`dish_id`))) join `windows` `w` on((`d`.`window_id` = `w`.`window_id`))) order by `o`.`created_at` desc;

-- ----------------------------
-- View structure for v_sales_ranking
-- ----------------------------
DROP VIEW IF EXISTS `v_sales_ranking`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_sales_ranking` AS select `d`.`dish_id` AS `dish_id`,`d`.`name` AS `name`,`d`.`price` AS `price`,`d`.`sales_count` AS `sales_count`,`c`.`name` AS `category_name`,`w`.`name` AS `window_name` from ((`dishes` `d` join `categories` `c` on((`d`.`category_id` = `c`.`category_id`))) join `windows` `w` on((`d`.`window_id` = `w`.`window_id`))) order by `d`.`sales_count` desc;

-- ----------------------------
-- View structure for v_sales_statistics
-- ----------------------------
DROP VIEW IF EXISTS `v_sales_statistics`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_sales_statistics` AS select cast(`o`.`created_at` as date) AS `sale_date`,count(distinct `o`.`order_id`) AS `order_count`,sum(`o`.`total_amount`) AS `total_sales`,avg(`o`.`total_amount`) AS `avg_order_amount`,sum(`oi`.`quantity`) AS `total_items_sold` from (`orders` `o` join `order_items` `oi` on((`o`.`order_id` = `oi`.`order_id`))) where (`o`.`status` = 4) group by cast(`o`.`created_at` as date) order by `sale_date` desc;

-- ----------------------------
-- View structure for v_user_nutrition_summary
-- ----------------------------
DROP VIEW IF EXISTS `v_user_nutrition_summary`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_user_nutrition_summary` AS select `o`.`user_id` AS `user_id`,`u`.`name` AS `user_name`,cast(`o`.`created_at` as date) AS `order_date`,sum((`n`.`calories` * `oi`.`quantity`)) AS `total_calories`,sum((`n`.`protein` * `oi`.`quantity`)) AS `total_protein`,sum((`n`.`fat` * `oi`.`quantity`)) AS `total_fat`,sum((`n`.`carbs` * `oi`.`quantity`)) AS `total_carbs` from ((((`orders` `o` join `order_items` `oi` on((`o`.`order_id` = `oi`.`order_id`))) join `dishes` `d` on((`oi`.`dish_id` = `d`.`dish_id`))) join `nutrition` `n` on((`d`.`dish_id` = `n`.`dish_id`))) join `users` `u` on((`o`.`user_id` = `u`.`user_id`))) where (`o`.`status` = 4) group by `o`.`user_id`,cast(`o`.`created_at` as date);

-- ----------------------------
-- View structure for v_user_orders
-- ----------------------------
DROP VIEW IF EXISTS `v_user_orders`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_user_orders` AS select `o`.`order_id` AS `order_id`,`o`.`user_id` AS `user_id`,`u`.`name` AS `user_name`,`o`.`total_amount` AS `total_amount`,`o`.`status` AS `status`,`o`.`pickup_code` AS `pickup_code`,`o`.`pickup_time` AS `pickup_time`,`o`.`created_at` AS `created_at`,`o`.`payment_time` AS `payment_time`,`o`.`finish_time` AS `finish_time` from (`orders` `o` join `users` `u` on((`o`.`user_id` = `u`.`user_id`)));

-- ----------------------------
-- Function structure for fn_calculate_recommend_score
-- ----------------------------
DROP FUNCTION IF EXISTS `fn_calculate_recommend_score`;
delimiter ;;
CREATE FUNCTION `fn_calculate_recommend_score`(p_favorite_count INT,
    p_browse_count INT,
    p_order_count INT,
    p_sales_count INT)
 RETURNS decimal(5,2)
  DETERMINISTIC
BEGIN
    RETURN ROUND(
        LEAST(p_favorite_count * 5, 30) + 
        LEAST(p_browse_count * 2, 20) + 
        LEAST(p_order_count * 3, 30) + 
        LEAST(p_sales_count * 0.1, 20), 
        2
    );
END
;;
delimiter ;

-- ----------------------------
-- Function structure for fn_get_nutrition_level
-- ----------------------------
DROP FUNCTION IF EXISTS `fn_get_nutrition_level`;
delimiter ;;
CREATE FUNCTION `fn_get_nutrition_level`(p_calories DECIMAL(8,2),
    p_protein DECIMAL(8,2),
    p_fat DECIMAL(8,2),
    p_goal INT)
 RETURNS varchar(20) CHARSET utf8mb4
  DETERMINISTIC
BEGIN
    IF p_goal = 1 THEN
        IF p_calories < 250 AND p_fat < 10 THEN
            RETURN '优秀';
        ELSEIF p_calories < 350 AND p_fat < 18 THEN
            RETURN '良好';
        ELSEIF p_calories < 450 THEN
            RETURN '一般';
        ELSE
            RETURN '不合适';
        END IF;
    ELSEIF p_goal = 2 THEN
        IF p_protein > 25 THEN
            RETURN '优秀';
        ELSEIF p_protein > 18 THEN
            RETURN '良好';
        ELSEIF p_protein > 12 THEN
            RETURN '一般';
        ELSE
            RETURN '不合适';
        END IF;
    ELSE
        IF p_calories BETWEEN 200 AND 400 AND p_protein > 15 AND p_fat < 20 THEN
            RETURN '优秀';
        ELSEIF p_calories BETWEEN 150 AND 500 AND p_protein > 10 THEN
            RETURN '良好';
        ELSE
            RETURN '一般';
        END IF;
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_ai_intelligent_meal
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_ai_intelligent_meal`;
delimiter ;;
CREATE PROCEDURE `sp_ai_intelligent_meal`(IN p_user_id BIGINT, IN p_meal_type TINYINT(1), OUT p_result JSON)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_ai_recommend_dishes
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_ai_recommend_dishes`;
delimiter ;;
CREATE PROCEDURE `sp_ai_recommend_dishes`(IN p_user_id BIGINT, IN p_limit INT)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_batch_update_stock
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_batch_update_stock`;
delimiter ;;
CREATE PROCEDURE `sp_batch_update_stock`(IN p_dish_ids TEXT, IN p_quantities TEXT)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_generate_pickup_code
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_generate_pickup_code`;
delimiter ;;
CREATE PROCEDURE `sp_generate_pickup_code`(OUT p_code VARCHAR(8))
BEGIN
    SET p_code = CONCAT(
        SUBSTRING('ABCDEFGHIJKLMNOPQRSTUVWXYZ', FLOOR(RAND() * 26) + 1, 1),
        SUBSTRING('ABCDEFGHIJKLMNOPQRSTUVWXYZ', FLOOR(RAND() * 26) + 1, 1),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10),
        FLOOR(RAND() * 10)
    );
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_get_health_evaluation
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_get_health_evaluation`;
delimiter ;;
CREATE PROCEDURE `sp_get_health_evaluation`(IN p_user_id BIGINT, OUT p_evaluation TEXT)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_get_low_stock_alert
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_get_low_stock_alert`;
delimiter ;;
CREATE PROCEDURE `sp_get_low_stock_alert`(IN p_threshold INT)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_get_student_preferences
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_get_student_preferences`;
delimiter ;;
CREATE PROCEDURE `sp_get_student_preferences`(OUT p_result JSON)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_get_user_today_nutrition
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_get_user_today_nutrition`;
delimiter ;;
CREATE PROCEDURE `sp_get_user_today_nutrition`(IN p_user_id BIGINT)
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
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_submit_order
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_submit_order`;
delimiter ;;
CREATE PROCEDURE `sp_submit_order`(IN p_user_id BIGINT,
    IN p_order_id VARCHAR(32),
    IN p_total_amount DECIMAL(10,2),
    IN p_pickup_time VARCHAR(50),
    IN p_items JSON,
    OUT p_result INT)
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
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table dishes
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_dish_add_log`;
delimiter ;;
CREATE TRIGGER `trg_dish_add_log` AFTER INSERT ON `dishes` FOR EACH ROW BEGIN
    INSERT INTO operation_logs (operation_type, operation_desc, table_name, record_id)
    VALUES ('ADD_DISH', CONCAT('新增菜品: ', NEW.name), 'dishes', CAST(NEW.dish_id AS CHAR));
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_items
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_order_deduct_stock`;
delimiter ;;
CREATE TRIGGER `trg_order_deduct_stock` AFTER INSERT ON `order_items` FOR EACH ROW BEGIN
    UPDATE dishes 
    SET stock = stock - NEW.quantity 
    WHERE dish_id = NEW.dish_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orders
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_order_create_log`;
delimiter ;;
CREATE TRIGGER `trg_order_create_log` AFTER INSERT ON `orders` FOR EACH ROW BEGIN
    INSERT INTO operation_logs (user_id, operation_type, operation_desc, table_name, record_id)
    VALUES (NEW.user_id, 'CREATE_ORDER', CONCAT('创建订单: ', NEW.order_id), 'orders', NEW.order_id);
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orders
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_cancel_order_restore_stock`;
delimiter ;;
CREATE TRIGGER `trg_cancel_order_restore_stock` AFTER UPDATE ON `orders` FOR EACH ROW BEGIN
    IF OLD.status != 5 AND NEW.status = 5 THEN
        UPDATE dishes d
        JOIN order_items oi ON d.dish_id = oi.dish_id
        SET d.stock = d.stock + oi.quantity
        WHERE oi.order_id = NEW.order_id;
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table orders
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_order_finish_update_sales`;
delimiter ;;
CREATE TRIGGER `trg_order_finish_update_sales` AFTER UPDATE ON `orders` FOR EACH ROW BEGIN
    IF OLD.status != 4 AND NEW.status = 4 THEN
        UPDATE dishes d
        JOIN order_items oi ON d.dish_id = oi.dish_id
        SET d.sales_count = d.sales_count + oi.quantity
        WHERE oi.order_id = NEW.order_id;
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table users
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_user_register_log`;
delimiter ;;
CREATE TRIGGER `trg_user_register_log` AFTER INSERT ON `users` FOR EACH ROW BEGIN
    INSERT INTO operation_logs (user_id, operation_type, operation_desc, table_name, record_id)
    VALUES (NEW.user_id, 'REGISTER', CONCAT('用户注册: ', NEW.account), 'users', CAST(NEW.user_id AS CHAR));
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
