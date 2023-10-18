package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "mix_udaf_param_item")
public class MixUdafParamItem extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -9143234002581892763L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 混合聚合函数参数id
     */
    private Integer mixUdafParamId;

    @RelationOneToOne(
            joinTable = "mix_udaf_param_item_base_udaf_param_relation",
            selfField = "id", joinSelfColumn = "mix_udaf_param_item_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id"
    )
    private BaseUdafParam baseUdafParam;

    @RelationOneToOne(
            joinTable = "mix_udaf_param_item_map_udaf_param_relation",
            selfField = "id", joinSelfColumn = "mix_udaf_param_item_id",
            targetField = "id", joinTargetColumn = "map_udaf_param_id")
    private MapUdafParam mapUdafParam;

    /**
     * 索引
     */
    private Integer sort;

}
