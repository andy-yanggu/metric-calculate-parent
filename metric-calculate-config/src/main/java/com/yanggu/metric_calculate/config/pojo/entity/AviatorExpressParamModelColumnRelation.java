package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * Aviator表达式和宽表字段中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("aviator_express_param_model_column_relation")
public class AviatorExpressParamModelColumnRelation extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 173292780784527110L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * Aviator表达式id
     */
    @Column("aviator_express_param_id")
    private Integer aviatorExpressParamId;

    /**
     * 宽表字段id
     */
    @Column("model_column_id")
    private Integer modelColumnId;

}
