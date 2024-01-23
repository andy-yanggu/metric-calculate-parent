package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "map_udaf_param_value_agg_relation")
public class MapUdafParamValueAggRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4367853687500259711L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 映射聚合函数参数id
     */
    private Integer mapUdafParamId;

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

}
