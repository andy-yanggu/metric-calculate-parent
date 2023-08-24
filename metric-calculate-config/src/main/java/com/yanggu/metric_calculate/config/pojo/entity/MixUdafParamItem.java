package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationManyToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "mix_udaf_param_item")
public class MixUdafParamItem extends BaseEntity implements Serializable {

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

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

    @RelationManyToOne(selfField = "baseUdafParamId", targetField = "id")
    private BaseUdafParam baseUdafParam;

    /**
     * 索引
     */
    private Integer sort;

}
