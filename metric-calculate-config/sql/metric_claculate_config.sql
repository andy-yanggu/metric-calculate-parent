/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : metric_claculate_config

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 07/07/2023 16:47:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aggregate_function
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function`;
CREATE TABLE `aggregate_function`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '聚合函数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aggregate_function
-- ----------------------------
INSERT INTO `aggregate_function` VALUES (1, 'SUM', '求和', NULL, 1, 0, '2023-07-07 14:41:35', '2023-07-07 14:41:38');

-- ----------------------------
-- Table structure for aggregate_function_param
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function_param`;
CREATE TABLE `aggregate_function_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_express_param
-- ----------------------------
DROP TABLE IF EXISTS `aviator_express_param`;
CREATE TABLE `aviator_express_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `express` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表达式',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Aviator表达式配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aviator_express_param
-- ----------------------------
INSERT INTO `aviator_express_param` VALUES (1, 'date_to_string(new java.util.Date(trans_timestamp), \'yyyy-MM-dd\')', 1, 0, '2023-07-07 14:16:37', '2023-07-07 14:24:05');
INSERT INTO `aviator_express_param` VALUES (2, 'long(date_to_string(new java.util.Date(trans_timestamp), \'HH\'))', 1, 0, '2023-07-07 14:18:18', '2023-07-07 14:24:07');
INSERT INTO `aviator_express_param` VALUES (8, 'amount > 100', 1, 0, '2023-07-07 14:31:39', '2023-07-07 14:31:39');
INSERT INTO `aviator_express_param` VALUES (9, 'amount', 1, 0, '2023-07-07 14:47:17', '2023-07-07 14:47:17');

-- ----------------------------
-- Table structure for aviator_function
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function`;
CREATE TABLE `aviator_function`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Aviator函数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aviator_function
-- ----------------------------
INSERT INTO `aviator_function` VALUES (1, 'coalesce', '合并函数', '返回第一个不为NULL的数据', 1, 0, '2023-07-06 18:15:16', '2023-07-06 18:17:22');

-- ----------------------------
-- Table structure for aviator_function_field
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function_field`;
CREATE TABLE `aviator_function_field`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段名',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `aviator_function_id` int(0) NOT NULL COMMENT 'Aviator函数id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Aviator函数字段模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_function_instance
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function_instance`;
CREATE TABLE `aviator_function_instance`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `aviator_function_id` int(0) NOT NULL COMMENT 'Aviator函数id',
  `param` json NULL COMMENT 'Aviator函数参数的JSON数据',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Aviator函数实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aviator_function_instance
-- ----------------------------
INSERT INTO `aviator_function_instance` VALUES (1, '', '', NULL, 1, NULL, 0, '2023-07-06 18:15:29', '2023-07-06 18:31:02');

-- ----------------------------
-- Table structure for base_udaf_param
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param`;
CREATE TABLE `base_udaf_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `param` json NULL COMMENT 'Aviator函数参数的JSON数据',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数值型、集合型、对象型聚合函数相关参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_udaf_param
-- ----------------------------
INSERT INTO `base_udaf_param` VALUES (1, 1, NULL, 0, '2023-07-07 14:41:49', '2023-07-07 14:41:52');

-- ----------------------------
-- Table structure for base_udaf_param_collective_sort_field_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_collective_sort_field_list_relation`;
CREATE TABLE `base_udaf_param_collective_sort_field_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `field_order_param_id` int(0) NOT NULL COMMENT '字段排序配置id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本聚合参数，排序字段列表（sortFieldList）中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param_distinct_field_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_distinct_field_list_relation`;
CREATE TABLE `base_udaf_param_distinct_field_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `field_order_param_id` int(0) NOT NULL COMMENT '字段排序配置id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本聚合参数，去重字段列表中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param_metric_express_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_metric_express_list_relation`;
CREATE TABLE `base_udaf_param_metric_express_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本聚合参数，多字段度量字段表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param_metric_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_metric_express_relation`;
CREATE TABLE `base_udaf_param_metric_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本聚合参数，度量字段表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_udaf_param_metric_express_relation
-- ----------------------------
INSERT INTO `base_udaf_param_metric_express_relation` VALUES (1, 1, 9, 0, '2023-07-07 14:47:25', '2023-07-07 14:47:25');

-- ----------------------------
-- Table structure for base_udaf_param_objective_compare_field_express_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_objective_compare_field_express_list_relation`;
CREATE TABLE `base_udaf_param_objective_compare_field_express_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本聚合参数，对象型比较字段列表中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param_retain_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_retain_express_relation`;
CREATE TABLE `base_udaf_param_retain_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基本聚合参数，保留字段表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive
-- ----------------------------
DROP TABLE IF EXISTS `derive`;
CREATE TABLE `derive`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `model_id` int(0) NULL DEFAULT NULL COMMENT '宽表id',
  `unit_measure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计量单位',
  `round_accuracy` int(0) NULL DEFAULT NULL COMMENT '精度',
  `round_accuracy_type` int(0) NULL DEFAULT NULL COMMENT '精度类型(0不处理 1四舍五入 2向上保留)',
  `data_type` int(0) NOT NULL DEFAULT 0 COMMENT '数据类型',
  `directory_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录编码',
  `include_current` int(0) NULL DEFAULT 0 COMMENT '是否包含当前笔',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '派生指标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of derive
-- ----------------------------
INSERT INTO `derive` VALUES (1, 'today_debit_asset', '当天借方金额总和', '当天借方金额总和', 1, NULL, NULL, NULL, 0, NULL, 0, 0, '2023-07-07 13:48:25', '2023-07-07 16:46:50');

-- ----------------------------
-- Table structure for derive_filter_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `derive_filter_express_relation`;
CREATE TABLE `derive_filter_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_id` int(0) NOT NULL COMMENT '派生指标id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '派生指标前置过滤条件中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of derive_filter_express_relation
-- ----------------------------
INSERT INTO `derive_filter_express_relation` VALUES (1, 1, 3, 0, '2023-07-07 13:49:39', '2023-07-07 14:31:43');

-- ----------------------------
-- Table structure for dimension
-- ----------------------------
DROP TABLE IF EXISTS `dimension`;
CREATE TABLE `dimension`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '维度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dimension_cloumn
-- ----------------------------
DROP TABLE IF EXISTS `dimension_cloumn`;
CREATE TABLE `dimension_cloumn`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `model_id` int(0) NOT NULL COMMENT '宽表id',
  `dimension_id` int(0) NOT NULL COMMENT '维度id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '维度字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for field_order_param
-- ----------------------------
DROP TABLE IF EXISTS `field_order_param`;
CREATE TABLE `field_order_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `aviator_express_param_id` int(0) NOT NULL COMMENT '表达式id',
  `is_asc` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否升序, true升序, false降序',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字段排序配置类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_udaf_param
-- ----------------------------
DROP TABLE IF EXISTS `map_udaf_param`;
CREATE TABLE `map_udaf_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `param` json NULL COMMENT 'Aviator函数参数的JSON数据',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '映射类型udaf参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_udaf_param_distinct_field_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `map_udaf_param_distinct_field_list_relation`;
CREATE TABLE `map_udaf_param_distinct_field_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式函数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '映射聚合参数，key的生成逻辑(去重字段列表)中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_udaf_param_value_agg_relation
-- ----------------------------
DROP TABLE IF EXISTS `map_udaf_param_value_agg_relation`;
CREATE TABLE `map_udaf_param_value_agg_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合函数参数id',
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param`;
CREATE TABLE `mix_udaf_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `param` json NULL COMMENT 'Aviator函数参数的JSON数据',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '混合类型udaf参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param_metric_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param_metric_express_relation`;
CREATE TABLE `mix_udaf_param_metric_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '混合聚合参数，多个聚合值的计算表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param_mix_agg_map_relation
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param_mix_agg_map_relation`;
CREATE TABLE `mix_udaf_param_mix_agg_map_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mix_udaf_param_id` int(0) NOT NULL COMMENT '混合聚合函数参数id',
  `key_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'map的key名称',
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '宽表名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `directory_id` int(0) NOT NULL COMMENT '目录id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据明细宽表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model
-- ----------------------------
INSERT INTO `model` VALUES (1, 'trade_detail', '交易流水表', NULL, 1, 1, 0, '2023-07-07 13:59:17', '2023-07-07 13:59:51');

-- ----------------------------
-- Table structure for model_column
-- ----------------------------
DROP TABLE IF EXISTS `model_column`;
CREATE TABLE `model_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中文名',
  `data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据类型(STRING、BOOLEAN、LONG、DOUBLE)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `field_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段类型(REAL、VIRTUAL)',
  `model_id` int(0) NULL DEFAULT NULL COMMENT '宽表id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `index` int(0) NOT NULL COMMENT '索引',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宽表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model_column
-- ----------------------------
INSERT INTO `model_column` VALUES (1, 'account_no_out', '转出账号', 'STRING', '转出账号', 'REAL', 1, 1, 1, 0, '2023-07-07 14:01:21', '2023-07-07 14:01:21');
INSERT INTO `model_column` VALUES (2, 'account_no_in', '转入账号', 'STRING', '转入账号', 'REAL', 1, 1, 2, 0, '2023-07-07 14:01:52', '2023-07-07 14:01:52');
INSERT INTO `model_column` VALUES (3, 'amount_no', '交易金额', 'DOUBLE', '交易金额', 'REAL', 1, 1, 3, 0, '2023-07-07 14:02:44', '2023-07-07 14:02:44');
INSERT INTO `model_column` VALUES (4, 'trans_timestamp', '交易时间戳', 'LONG', '交易时间戳', 'REAL', 1, 1, 4, 0, '2023-07-07 14:03:22', '2023-07-07 14:13:56');
INSERT INTO `model_column` VALUES (5, 'trans_date', '交易日期', 'STRING', '交易日期', 'VIRTUAL', 1, 1, 5, 0, '2023-07-07 14:04:22', '2023-07-07 14:05:23');
INSERT INTO `model_column` VALUES (6, 'trans_hour', '交易小时数', 'LONG', '交易小时数', 'VIRTUAL', 1, 1, 6, 0, '2023-07-07 14:05:18', '2023-07-07 14:05:18');

-- ----------------------------
-- Table structure for model_column_aviator_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `model_column_aviator_express_relation`;
CREATE TABLE `model_column_aviator_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `model_column_id` int(0) NOT NULL COMMENT '宽表字段id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宽表字段表达式关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model_column_aviator_express_relation
-- ----------------------------
INSERT INTO `model_column_aviator_express_relation` VALUES (1, 5, 1, 0, '2023-07-07 14:16:51', '2023-07-07 14:24:16');
INSERT INTO `model_column_aviator_express_relation` VALUES (2, 6, 2, 0, '2023-07-07 14:18:26', '2023-07-07 14:24:19');

-- ----------------------------
-- Table structure for tbl_constant_metrics
-- ----------------------------
DROP TABLE IF EXISTS `tbl_constant_metrics`;
CREATE TABLE `tbl_constant_metrics`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '常量指标中文名称',
  `constant_metrics_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常量指标编码',
  `source_table_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源表名称，只做标记',
  `update_cycle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新周期，只做标记使用',
  `time_store_granularity` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时间粒度',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态(备用0:未发布,1已发布,2旧版本)',
  `directory_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录编码',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `unit_measure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计量单位',
  `data_type` int(0) NOT NULL DEFAULT 0 COMMENT '数据类型',
  `contain_current` int(0) NULL DEFAULT 0 COMMENT '是否包含当前笔',
  `store_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存储宽表编码',
  `tenant_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '常量指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_constant_metrics_dimension_re
-- ----------------------------
DROP TABLE IF EXISTS `tbl_constant_metrics_dimension_re`;
CREATE TABLE `tbl_constant_metrics_dimension_re`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `constant_metrics_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常量指标编码',
  `dimension_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度code',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本号',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '常量指标维度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_constant_metrics_item
-- ----------------------------
DROP TABLE IF EXISTS `tbl_constant_metrics_item`;
CREATE TABLE `tbl_constant_metrics_item`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `constant_metrics_item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '常量指标项编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '常量指标项名',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '常量指标项中文名称',
  `constant_metrics_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常量指标编码',
  `store_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存储宽表编码',
  `store_column_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存储宽表字段编码',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态(备用0:未发布,1已发布,2旧版本)',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本号',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_constant_metrics_store_re
-- ----------------------------
DROP TABLE IF EXISTS `tbl_constant_metrics_store_re`;
CREATE TABLE `tbl_constant_metrics_store_re`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `constant_metrics_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常量指标编码',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态(备用0:未发布,1已发布,2旧版本)',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本号',
  `store_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标存储宽表编码外键',
  `store_column_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标存储字段表编码',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '常量指标对存储宽表关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_derive_metrics_column
-- ----------------------------
DROP TABLE IF EXISTS `tbl_derive_metrics_column`;
CREATE TABLE `tbl_derive_metrics_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_metrics_column_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '派生字段关联编码',
  `derive_metrics_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '派生指标编码',
  `atom_column_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原子字段关联编码',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态(备用0:未发布,1已发布,2旧版本)',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '派生字段关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_derive_metrics_store_re
-- ----------------------------
DROP TABLE IF EXISTS `tbl_derive_metrics_store_re`;
CREATE TABLE `tbl_derive_metrics_store_re`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_metrics_code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '派生指标编码',
  `store_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标存储宽表编码外键',
  `store_column_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标存储字段表编码',
  `status` int(0) NULL DEFAULT NULL COMMENT '发布状态(0:未发布;1:已发布)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本号',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '派生指标对存储宽表关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_dimension_filter
-- ----------------------------
DROP TABLE IF EXISTS `tbl_dimension_filter`;
CREATE TABLE `tbl_dimension_filter`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `metrics_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指标编码',
  `metrics_type` int(0) NULL DEFAULT NULL COMMENT '指标类型',
  `column_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段编码',
  `sign` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计算符号',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度值',
  `version` int(0) NULL DEFAULT NULL COMMENT '版本号',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '限定维度过滤表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_dimension_item
-- ----------------------------
DROP TABLE IF EXISTS `tbl_dimension_item`;
CREATE TABLE `tbl_dimension_item`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子项字典编码',
  `dimension_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度编码外键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子项字典名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子项字典值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典维度子项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_directory
-- ----------------------------
DROP TABLE IF EXISTS `tbl_directory`;
CREATE TABLE `tbl_directory`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `directory_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录编码',
  `parent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父目录编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录名称',
  `type` int(0) NULL DEFAULT NULL COMMENT '目录类型(0 原子指标 1是派生指标)',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态(备用)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(0未删除)',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '目录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for time_cloumn
-- ----------------------------
DROP TABLE IF EXISTS `time_cloumn`;
CREATE TABLE `time_cloumn`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `model_id` int(0) NOT NULL COMMENT '宽表id',
  `time_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '时间字段' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
