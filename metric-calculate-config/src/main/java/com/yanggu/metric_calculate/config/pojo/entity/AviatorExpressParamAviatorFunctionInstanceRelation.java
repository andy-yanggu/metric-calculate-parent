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
 * Aviator函数和Aviator函数实例中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("aviator_express_param_aviator_function_instance_relation")
public class AviatorExpressParamAviatorFunctionInstanceRelation extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1246393150898778928L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * Aviator表达式id
     */
    @Column("aviator_express_param_id")
    private Integer aviatorExpressParamId;

    /**
     * Aviator函数实例id
     */
    @Column("aviator_function_instance_id")
    private Integer aviatorFunctionInstanceId;

}
