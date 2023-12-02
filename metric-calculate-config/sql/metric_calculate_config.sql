CREATE TABLE atom (
                      id int NOT NULL AUTO_INCREMENT,
                      name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                      display_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
                      description varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
                      model_id int NOT NULL COMMENT '宽表id',
                      model_time_column_id int NOT NULL COMMENT '宽表时间字段id',
                      directory_code varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录编码',
                      user_id int NOT NULL COMMENT '用户id',
                      is_deleted int NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
                      create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                      PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='原子指标';

CREATE TABLE atom_aggregate_function_param_relation (
                                                        id int NOT NULL AUTO_INCREMENT,
                                                        atom_id int NOT NULL COMMENT '原子指标id',
                                                        aggregate_function_param_id int NOT NULL COMMENT '聚合函数参数id',
                                                        user_id int NOT NULL COMMENT '用户id',
                                                        is_deleted int NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
                                                        create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                        update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                        PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='原子指标聚合函数参数中间表';

-- ----------------------------
-- Table structure for aggregate_function
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function`;
CREATE TABLE `aggregate_function`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聚合函数类型（数值、集合、对象、混合、映射）',
  `key_strategy` tinyint(0) NULL DEFAULT 0 COMMENT '集合型和对象型主键策略（0没有主键、1去重字段、2排序字段、3比较字段）',
  `retain_strategy` tinyint(0) NULL DEFAULT 0 COMMENT '集合型和对象型保留字段策略（0不保留任何数据、1保留指定字段、2保留原始数据）',
  `multi_number` tinyint(0) NULL DEFAULT 0 COMMENT '数值型是否需要多个参数（0否，1是需要多个例如协方差）',
  `is_built_in` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否内置：0否，1是',
  `jar_store_id` int(0) NULL DEFAULT NULL COMMENT 'jar存储id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聚合函数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aggregate_function_field
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function_field`;
CREATE TABLE `aggregate_function_field`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `sort` int(0) NOT NULL COMMENT '索引',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聚合函数的字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aggregate_function_param
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function_param`;
CREATE TABLE `aggregate_function_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聚合函数参数配置类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aggregate_function_param_base_udaf_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function_param_base_udaf_param_relation`;
CREATE TABLE `aggregate_function_param_base_udaf_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_param_id` int(0) NOT NULL COMMENT '聚合函数参数id',
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聚合函数参数-基本聚合参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aggregate_function_param_map_udaf_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function_param_map_udaf_param_relation`;
CREATE TABLE `aggregate_function_param_map_udaf_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_param_id` int(0) NOT NULL COMMENT '聚合函数参数id',
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聚合函数参数-映射聚合参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aggregate_function_param_mix_udaf_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `aggregate_function_param_mix_udaf_param_relation`;
CREATE TABLE `aggregate_function_param_mix_udaf_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_param_id` int(0) NOT NULL COMMENT '聚合函数参数id',
  `mix_udaf_param_id` int(0) NOT NULL COMMENT '混合聚合参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聚合函数参数-混合聚合参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_express_param
-- ----------------------------
DROP TABLE IF EXISTS `aviator_express_param`;
CREATE TABLE `aviator_express_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `express` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表达式',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator表达式配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_express_param_aviator_function_instance_relation
-- ----------------------------
DROP TABLE IF EXISTS `aviator_express_param_aviator_function_instance_relation`;
CREATE TABLE `aviator_express_param_aviator_function_instance_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `aviator_function_instance_id` int(0) NOT NULL COMMENT 'Aviator函数实例id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator函数和Aviator函数实例中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_express_param_mix_udaf_param_item_relation
-- ----------------------------
DROP TABLE IF EXISTS `aviator_express_param_mix_udaf_param_item_relation`;
CREATE TABLE `aviator_express_param_mix_udaf_param_item_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `mix_udaf_param_item_id` int(0) NOT NULL COMMENT '混合类型参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator表达式和混合类型参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_express_param_model_column_relation
-- ----------------------------
DROP TABLE IF EXISTS `aviator_express_param_model_column_relation`;
CREATE TABLE `aviator_express_param_model_column_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `model_column_id` int(0) NOT NULL COMMENT '宽表字段id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator表达式和宽表字段中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_function
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function`;
CREATE TABLE `aviator_function`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `jar_store_id` int(0) NULL DEFAULT NULL COMMENT 'jar配置id',
  `is_built_in` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否内置：0否，1是',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator函数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_function_field
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function_field`;
CREATE TABLE `aviator_function_field`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `aviator_function_id` int(0) NOT NULL COMMENT 'Aviator函数id',
  `sort` int(0) NOT NULL COMMENT '索引',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator函数字段模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aviator_function_instance
-- ----------------------------
DROP TABLE IF EXISTS `aviator_function_instance`;
CREATE TABLE `aviator_function_instance`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `aviator_function_id` int(0) NOT NULL COMMENT 'Aviator函数id',
  `param` json NULL COMMENT 'Aviator函数参数的JSON数据',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Aviator函数实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param`;
CREATE TABLE `base_udaf_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `param` json NULL COMMENT '聚合函数参数的JSON数据',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数值型、集合型、对象型聚合函数相关参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param_metric_express_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_metric_express_list_relation`;
CREATE TABLE `base_udaf_param_metric_express_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基本聚合参数，多字段度量字段表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_udaf_param_metric_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `base_udaf_param_metric_express_relation`;
CREATE TABLE `base_udaf_param_metric_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基本聚合参数，度量字段表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive
-- ----------------------------
DROP TABLE IF EXISTS `derive`;
CREATE TABLE `derive`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `model_id` int(0) NOT NULL COMMENT '宽表id',
  `include_current` int(0) NOT NULL DEFAULT 1 COMMENT '是否包含当前笔，0不包含，1包含',
  `unit_measure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `round_accuracy_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NOT_HANDLE' COMMENT '精度类型(NOT_HANDLE（不处理）、ROUNDING（四舍五入）、KEEP_UP（向上保留）)',
  `round_accuracy_length` int(0) NOT NULL DEFAULT 0 COMMENT '精度长度',
  `data_type` int(0) NOT NULL DEFAULT 0 COMMENT '数据类型',
  `directory_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录编码',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '派生指标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive_aggregate_function_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `derive_aggregate_function_param_relation`;
CREATE TABLE `derive_aggregate_function_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_id` int(0) NOT NULL COMMENT '派生指标id',
  `aggregate_function_param_id` int(0) NOT NULL COMMENT '聚合函数参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '派生指标聚合函数参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive_filter_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `derive_filter_express_relation`;
CREATE TABLE `derive_filter_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_id` int(0) NOT NULL COMMENT '派生指标id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '派生指标前置过滤条件中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive_model_dimension_column_relation
-- ----------------------------
DROP TABLE IF EXISTS `derive_model_dimension_column_relation`;
CREATE TABLE `derive_model_dimension_column_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `derive_id` int(0) NOT NULL COMMENT '派生指标id',
  `model_dimension_column_id` int(0) NOT NULL COMMENT '维度字段id',
  `sort` int(0) NOT NULL DEFAULT 1 COMMENT '序号',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '派生指标维度字段中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive_model_time_column_relation
-- ----------------------------
DROP TABLE IF EXISTS `derive_model_time_column_relation`;
CREATE TABLE `derive_model_time_column_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_id` int(0) NOT NULL COMMENT '派生指标id',
  `model_time_column_id` int(0) NOT NULL COMMENT '时间字段id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '派生指标和时间字段中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for derive_window_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `derive_window_param_relation`;
CREATE TABLE `derive_window_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `derive_id` int(0) NOT NULL COMMENT '派生指标id',
  `window_param_id` int(0) NOT NULL COMMENT '窗口参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '派生指标-窗口参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for modelDimensionColumn
-- ----------------------------
DROP TABLE IF EXISTS `modelDimensionColumn`;
CREATE TABLE `modelDimensionColumn`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 212 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jar_store
-- ----------------------------
DROP TABLE IF EXISTS `jar_store`;
CREATE TABLE `jar_store`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `jar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'jar包url',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'jar包存储' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_udaf_param
-- ----------------------------
DROP TABLE IF EXISTS `map_udaf_param`;
CREATE TABLE `map_udaf_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `param` json NULL COMMENT '聚合函数参数的JSON数据',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '映射类型udaf参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_udaf_param_distinct_field_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `map_udaf_param_distinct_field_list_relation`;
CREATE TABLE `map_udaf_param_distinct_field_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式函数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '映射聚合参数，key的生成逻辑(去重字段列表)中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_udaf_param_value_agg_relation
-- ----------------------------
DROP TABLE IF EXISTS `map_udaf_param_value_agg_relation`;
CREATE TABLE `map_udaf_param_value_agg_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合函数参数id',
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合函数参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param`;
CREATE TABLE `mix_udaf_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `aggregate_function_id` int(0) NOT NULL COMMENT '聚合函数id',
  `param` json NULL COMMENT '聚合函数参数的JSON数据',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '混合类型udaf参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param_item
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param_item`;
CREATE TABLE `mix_udaf_param_item`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `mix_udaf_param_id` int(0) NOT NULL COMMENT '混合聚合函数参数id',
  `sort` int(0) NOT NULL COMMENT '索引',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param_item_base_udaf_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param_item_base_udaf_param_relation`;
CREATE TABLE `mix_udaf_param_item_base_udaf_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mix_udaf_param_item_id` int(0) NOT NULL COMMENT '混合聚合参数选项id',
  `base_udaf_param_id` int(0) NOT NULL COMMENT '基本聚合参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '混合聚合参数选项-基本聚合参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param_item_map_udaf_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param_item_map_udaf_param_relation`;
CREATE TABLE `mix_udaf_param_item_map_udaf_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mix_udaf_param_item_id` int(0) NOT NULL COMMENT '混合聚合参数选项id',
  `map_udaf_param_id` int(0) NOT NULL COMMENT '映射聚合参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '混合聚合参数选项-映射聚合参数中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mix_udaf_param_metric_express_relation
-- ----------------------------
DROP TABLE IF EXISTS `mix_udaf_param_metric_express_relation`;
CREATE TABLE `mix_udaf_param_metric_express_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `mix_udaf_param_id` int(0) NOT NULL COMMENT '混合聚合函数参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator函数参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '混合聚合参数，多个聚合值的计算表达式中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '宽表名称',
  `display_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `directory_id` int(0) NOT NULL COMMENT '目录id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据明细宽表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_column
-- ----------------------------
DROP TABLE IF EXISTS `model_column`;
CREATE TABLE `model_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名',
  `data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型(STRING、BOOLEAN、LONG、DOUBLE)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `field_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段类型(REAL、VIRTUAL)',
  `model_id` int(0) NULL DEFAULT NULL COMMENT '宽表id',
  `sort` int(0) NOT NULL COMMENT '索引',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宽表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_column_aviator_express_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `model_column_aviator_express_param_relation`;
CREATE TABLE `model_column_aviator_express_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `model_column_id` int(0) NOT NULL COMMENT '宽表字段id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宽表字段表达式关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_dimension_column
-- ----------------------------
DROP TABLE IF EXISTS `model_dimension_column`;
CREATE TABLE `model_dimension_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `model_id` int(0) NOT NULL COMMENT '宽表id',
  `model_column_id` int(0) NOT NULL COMMENT '宽表字段id',
  `dimension_id` int(0) NOT NULL COMMENT '维度id',
  `sort` int(0) NOT NULL COMMENT '索引',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宽表维度字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_time_column
-- ----------------------------
DROP TABLE IF EXISTS `model_time_column`;
CREATE TABLE `model_time_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `model_id` int(0) NOT NULL COMMENT '宽表id',
  `model_column_id` int(0) NOT NULL COMMENT '宽表字段id',
  `time_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '时间格式',
  `sort` int(0) NOT NULL COMMENT '索引',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宽表时间字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for node_pattern
-- ----------------------------
DROP TABLE IF EXISTS `node_pattern`;
CREATE TABLE `node_pattern`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `interval` int(0) NOT NULL DEFAULT 0 COMMENT '间隔时间（单位毫秒值）',
  `sort` int(0) NOT NULL COMMENT '索引',
  `window_param_id` int(0) NOT NULL COMMENT '窗口参数id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CEP匹配配置数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for node_pattern_aviator_express_param_relation
-- ----------------------------
DROP TABLE IF EXISTS `node_pattern_aviator_express_param_relation`;
CREATE TABLE `node_pattern_aviator_express_param_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `node_pattern_id` int(0) NOT NULL COMMENT 'CEP匹配配置数据id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CEP匹配配置数据表达式关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for window_param
-- ----------------------------
DROP TABLE IF EXISTS `window_param`;
CREATE TABLE `window_param`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `window_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '窗口类型',
  `model_time_column_id` int(0) NULL DEFAULT NULL COMMENT '宽表时间字段id',
  `duration` int(0) NULL DEFAULT NULL COMMENT '时间周期',
  `time_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间单位',
  `sliding_count` int(0) NULL DEFAULT NULL COMMENT '滑动计数窗口大小',
  `gap_time_millis` int(0) NULL DEFAULT NULL COMMENT '会话窗口间隔（时间单位毫秒值）',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '窗口相关参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for window_param_status_express_param_list_relation
-- ----------------------------
DROP TABLE IF EXISTS `window_param_status_express_param_list_relation`;
CREATE TABLE `window_param_status_express_param_list_relation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `window_param_id` int(0) NOT NULL COMMENT '窗口参数id',
  `aviator_express_param_id` int(0) NOT NULL COMMENT 'Aviator表达式id',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除(缺省为0,即未删除)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '窗口参数状态窗口表达式列表关系表' ROW_FORMAT = Dynamic;
