package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 混合类型udaf参数 实体类。
 *
 * @author MondayLi
 * @since 2023-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "mix_udaf_param")
public class MixUdafParam implements Serializable {

    
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationOneToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunction aggregateFunction;

    /**
     * 混合聚合类型定义
     * <p>k是名字, value是基本聚合类型的参数, 用于定义聚合计算逻辑</p>
     */
    @RelationOneToMany(
            joinTable = "mix_udaf_param_mix_agg_map_relation",
            selfField = "id", joinSelfColumn = "mix_udaf_param_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id",
            mapKeyField = "key_name"
    )
    private Map<String, AviatorExpressParam> mixAggMap;

    /**
     * 多个聚合值的计算表达式
     */
    @RelationOneToOne(
            joinTable = "mix_udaf_param_metric_express_relation",
            selfField = "id", joinSelfColumn = "mix_udaf_param_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id"
    )
    private AviatorExpressParam metricExpressParam;

    /**
     * Aviator函数参数的JSON数据
     */
    private String param;

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
