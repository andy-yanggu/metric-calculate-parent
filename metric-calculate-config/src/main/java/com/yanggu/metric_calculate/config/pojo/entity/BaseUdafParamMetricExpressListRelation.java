package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基本聚合参数，多字段度量字段表达式中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "base_udaf_param_metric_express_list_relation")
public class BaseUdafParamMetricExpressListRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6245258082182219046L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

    /**
     * Aviator函数参数id
     */
    private Integer aviatorExpressParamId;

}
