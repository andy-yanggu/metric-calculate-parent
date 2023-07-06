SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Table structure for aviator_function_instance
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function_instance`;
CREATE TABLE `aviator_function_instance`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `aviator_function_id` int(0) NOT NULL COMMENT 'Aviator函数id',
  `aviator_express_param_id` int(0) NULL DEFAULT NULL COMMENT 'Aviator表达式配置id',
  `param` json NULL COMMENT 'Aviator函数参数的JSON数据',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Aviator函数实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_function_param
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function_param`;
CREATE TABLE `aviator_function_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段名',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `aviator_function_id` int(0) NOT NULL COMMENT 'Aviator函数id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Aviator函数字段模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive
-- ----------------------------
DROP TABLE IF EXISTS `derive`;
CREATE TABLE `derive`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中文名称',
  `code` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '派生指标编码',
  `calculate_logic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计算逻辑表达式',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `time_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时间聚合粒度',
  `duration` int(0) NULL DEFAULT NULL COMMENT '聚合时间长度',
  `unit_measure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计量单位',
  `time_store_granularity` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时间存储粒度',
  `round_accuracy` int(0) NULL DEFAULT NULL COMMENT '精度',
  `round_accuracy_type` int(0) NULL DEFAULT NULL COMMENT '精度类型(0不处理 1四舍五入 2向上保留)',
  `data_type` int(0) NOT NULL DEFAULT 0 COMMENT '数据类型',
  `directory_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录编码',
  `include_current` int(0) NULL DEFAULT 0 COMMENT '是否包含当前笔',
  `window_type` int(0) NULL DEFAULT 0 COMMENT '窗口类型，0：时间窗口，1：数量窗口',
  `window_count` int(0) NULL DEFAULT NULL COMMENT '窗口数量',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1443 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '派生指标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '宽表编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '宽表名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `directory_id` int(0) NOT NULL COMMENT '目录id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 239 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据明细宽表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_column
-- ----------------------------
DROP TABLE IF EXISTS `model_column`;
CREATE TABLE `model_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型字段编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中文名',
  `data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据类型(STRING、BOOLEAN、LONG、DOUBLE)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `field_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段类型(REAL、VIRTUAL)',
  `model_id` int(0) NULL DEFAULT NULL COMMENT '宽表id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `index` int(0) NULL DEFAULT NULL COMMENT '索引',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宽表字段' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '常量指标表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 121 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '常量指标维度表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1782 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '派生字段关联表' ROW_FORMAT = Dynamic;

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
-- Table structure for tbl_dimension
-- ----------------------------
DROP TABLE IF EXISTS `tbl_dimension`;
CREATE TABLE `tbl_dimension`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `dimension_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维度名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '中文名称',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态(备用0:未发布,1已发布,2旧版本)',
  `type` int(0) NULL DEFAULT NULL COMMENT '维度类型（字典维度 业务维度 时间维度）',
  `time_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时间格式',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '维度字段表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典维度子项表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 266 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '目录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
