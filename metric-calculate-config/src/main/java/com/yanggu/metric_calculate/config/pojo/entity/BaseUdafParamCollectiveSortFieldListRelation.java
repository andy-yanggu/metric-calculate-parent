package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基本聚合参数，排序字段列表（sortFieldList）中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "base_udaf_param_collective_sort_field_list_relation")
public class BaseUdafParamCollectiveSortFieldListRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2694951012921623490L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

    /**
     * 字段排序配置id
     */
    private Integer fieldOrderParamId;

}
