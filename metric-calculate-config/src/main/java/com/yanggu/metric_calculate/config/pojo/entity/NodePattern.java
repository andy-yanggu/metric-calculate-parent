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
 * CEP匹配配置数据
 */
@Data
@Table(value = "node_pattern")
@EqualsAndHashCode(callSuper = true)
public class NodePattern extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2248230353147672225L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * Aviator表达式参数
     */
    @RelationOneToOne(
            joinTable = "node_pattern_aviator_express_param_relation",
            selfField = "id", joinSelfColumn = "node_pattern_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParam matchExpressParam;

    /**
     * 间隔时间（单位毫秒值）
     */
    private Long interval;

    /**
     * 索引
     */
    private Integer sort;

    /**
     * 窗口参数id
     */
    private Integer windowParamId;

}