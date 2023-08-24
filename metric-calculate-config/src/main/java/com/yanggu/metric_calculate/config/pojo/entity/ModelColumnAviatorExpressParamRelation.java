package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 宽表字段表达式关系表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "model_column_aviator_express_param_relation")
public class ModelColumnAviatorExpressParamRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4156625855782096027L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 宽表字段id
     */
    private Integer modelColumnId;

    /**
     * Aviator表达式id
     */
    private Integer aviatorExpressParamId;

}
