package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "mix_udaf_param_item")
public class MixUdafParamItem implements Serializable {

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

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    @Column(onInsertValue = "0", isLogicDelete = true)
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP", onUpdateValue = "CURRENT_TIMESTAMP")
    private Date updateTime;

}
