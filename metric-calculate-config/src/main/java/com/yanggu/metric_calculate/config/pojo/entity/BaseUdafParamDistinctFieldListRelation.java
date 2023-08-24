package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基本聚合参数，去重字段列表中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "base_udaf_param_distinct_field_list_relation")
public class BaseUdafParamDistinctFieldListRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4855021571374849578L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

    /**
     * Aviator函数参数id
     */
    private Integer aviatorExpressParamId;

}
