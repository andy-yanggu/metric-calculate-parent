package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import com.yanggu.metric_calculate.config.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 宽表字段 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "model_column")
public class ModelColumn implements Serializable {

    private static final long serialVersionUID = 1424390731821599400L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 数据类型(STRING、BOOLEAN、LONG、DOUBLE)
     */
    private DataType dataType;

    /**
     * 描述
     */
    private String description;

    /**
     * 字段类型(REAL、VIRTUAL)
     */
    private String fieldType;

    /**
     * 宽表id
     */
    private Integer modelId;

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

    /**
     * 如果是虚拟字段，增加Aviator表达式
     */
    @RelationOneToOne(
            joinTable = "model_column_aviator_express_relation",
            selfField = "id", joinSelfColumn = "model_column_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParam aviatorExpressParam;

}
