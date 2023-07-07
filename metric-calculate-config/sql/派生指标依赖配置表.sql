-- 派生指标前置过滤条件
CREATE TABLE `derive_filter_express_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `derive_id`                int      NOT NULL COMMENT '派生指标id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator表达式id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='派生指标前置过滤条件中间表';

-- 基本聚合参数
CREATE TABLE `base_udaf_param`
(
    `id`                    int      NOT NULL AUTO_INCREMENT,
    `aggregate_function_id` int      NOT NULL COMMENT '聚合函数id',
    `param`                 json              DEFAULT NULL COMMENT 'Aviator函数参数的JSON数据',
    `is_deleted`            int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='数值型、集合型、对象型聚合函数相关参数';

-- 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
CREATE TABLE `base_udaf_param_metric_express_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `base_udaf_param_id`       int      NOT NULL COMMENT '基本聚合函数参数id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator函数参数id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='基本聚合参数，度量字段表达式中间表';

-- 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
CREATE TABLE `base_udaf_param_metric_express_list_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `base_udaf_param_id`       int      NOT NULL COMMENT '基本聚合函数参数id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator函数参数id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='基本聚合参数，多字段度量字段表达式中间表';

-- 保留字段表达式（retainExpress）：对象型和集合型只保留指定字段的值
CREATE TABLE `base_udaf_param_retain_express_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `base_udaf_param_id`       int      NOT NULL COMMENT '基本聚合函数参数id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator函数参数id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='基本聚合参数，保留字段表达式中间表';

-- 对象型比较字段列表(对象型最大对象、最小对象)
CREATE TABLE `base_udaf_param_objective_compare_field_express_list_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `base_udaf_param_id`       int      NOT NULL COMMENT '基本聚合函数参数id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator函数参数id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='基本聚合参数，对象型比较字段列表中间表';

-- 字段排序配置类
CREATE TABLE `field_order_param`
(
    `id`                       int      NOT NULL AUTO_INCREMENT COMMENT '主键自增',
    `aviator_express_param_id` int      NOT NULL COMMENT '表达式id',
    `is_asc`                   tinyint  NOT NULL DEFAULT '0' COMMENT '是否升序, true升序, false降序',
    `user_id`                  int      NOT NULL COMMENT '用户id',
    `is_deleted`               tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='字段排序配置类';

-- 排序字段列表（sortFieldList）：类似SQL中的ORDER BY id ASC, user_name DESC，多字段排序。
CREATE TABLE `base_udaf_param_collective_sort_field_list_relation`
(
    `id`                   int      NOT NULL AUTO_INCREMENT,
    `base_udaf_param_id`   int      NOT NULL COMMENT '基本聚合函数参数id',
    `field_order_param_id` int      NOT NULL COMMENT '字段排序配置id',
    `is_deleted`           int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`         datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='基本聚合参数，排序字段列表（sortFieldList）中间表';

-- 去重字段列表（distinctFieldList）：根据多个字段进行去重。集合型（去重列表）
CREATE TABLE `base_udaf_param_distinct_field_list_relation`
(
    `id`                   int      NOT NULL AUTO_INCREMENT,
    `base_udaf_param_id`   int      NOT NULL COMMENT '基本聚合函数参数id',
    `field_order_param_id` int      NOT NULL COMMENT '字段排序配置id',
    `is_deleted`           int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`         datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='基本聚合参数，去重字段列表中间表';

-- 映射类型udaf参数
CREATE TABLE `map_udaf_param`
(
    `id`                    int      NOT NULL AUTO_INCREMENT,
    `aggregate_function_id` int      NOT NULL COMMENT '聚合函数id',
    `param`                 json              DEFAULT NULL COMMENT 'Aviator函数参数的JSON数据',
    `is_deleted`            int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='映射类型udaf参数';

-- key的生成逻辑(去重字段列表)
CREATE TABLE `map_udaf_param_distinct_field_list_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `map_udaf_param_id`        int      NOT NULL COMMENT '映射聚合函数参数id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator表达式函数id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='映射聚合参数，key的生成逻辑(去重字段列表)中间表';

-- value的聚合函数参数。只能是数值型、集合型、对象型
CREATE TABLE `map_udaf_param_value_agg_relation`
(
    `id`                 int      NOT NULL AUTO_INCREMENT,
    `map_udaf_param_id`  int      NOT NULL COMMENT '映射聚合函数参数id',
    `base_udaf_param_id` int      NOT NULL COMMENT '基本聚合函数参数id',
    `is_deleted`         int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`       datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表';

-- 混合类型udaf参数
CREATE TABLE `mix_udaf_param`
(
    `id`                    int      NOT NULL AUTO_INCREMENT,
    `aggregate_function_id` int      NOT NULL COMMENT '聚合函数id',
    `param`                 json              DEFAULT NULL COMMENT 'Aviator函数参数的JSON数据',
    `is_deleted`            int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`          datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='混合类型udaf参数';

-- 混合聚合类型定义
CREATE TABLE `mix_udaf_param_mix_agg_map_relation`
(
    `id`                 INT          NOT NULL AUTO_INCREMENT,
    `mix_udaf_param_id`  INT          NOT NULL COMMENT '混合聚合函数参数id',
    `key_name`           VARCHAR(255) NOT NULL COMMENT 'map的key名称',
    `base_udaf_param_id` INT          NOT NULL COMMENT '基本聚合函数参数id',
    `is_deleted`         INT          NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表';

-- 多个聚合值的计算表达式
CREATE TABLE `map_udaf_param_metric_express_relation`
(
    `id`                       int      NOT NULL AUTO_INCREMENT,
    `map_udaf_param_id`        int      NOT NULL COMMENT '映射聚合函数参数id',
    `aviator_express_param_id` int      NOT NULL COMMENT 'Aviator函数参数id',
    `is_deleted`               int      NOT NULL DEFAULT '0' COMMENT '是否删除(缺省为0,即未删除)',
    `created_time`             datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='映射聚合参数，多个聚合值的计算表达式中间表';
