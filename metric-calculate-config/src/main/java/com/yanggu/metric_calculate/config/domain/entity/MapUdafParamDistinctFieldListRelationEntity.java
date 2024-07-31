package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 映射聚合参数，key的生成逻辑(去重字段列表)中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "map_udaf_param_distinct_field_list_relation")
public class MapUdafParamDistinctFieldListRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2730189628091826115L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 映射聚合函数参数id
     */
    private Integer mapUdafParamId;

    /**
     * Aviator表达式函数id
     */
    private Integer aviatorExpressParamId;

}
