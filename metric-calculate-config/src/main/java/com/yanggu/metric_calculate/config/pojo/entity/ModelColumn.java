package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.enums.DataType;
import com.yanggu.metric_calculate.config.enums.ModelColumnFieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表字段 实体类。
 */
@Data
@Table(value = "model_column")
@EqualsAndHashCode(callSuper = true)
public class ModelColumn extends BaseEntity implements Serializable {

    @Serial
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
    private ModelColumnFieldType fieldType;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 索引
     */
    private Integer sort;

    /**
     * 如果是虚拟字段，增加Aviator表达式
     */
    @RelationOneToOne(
            joinTable = "model_column_aviator_express_param_relation",
            selfField = "id", joinSelfColumn = "model_column_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParam aviatorExpressParam;

}
