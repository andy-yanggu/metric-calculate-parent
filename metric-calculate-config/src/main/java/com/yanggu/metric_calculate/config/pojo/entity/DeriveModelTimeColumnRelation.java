package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 派生指标和时间字段中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "derive_model_time_column_relation")
public class DeriveModelTimeColumnRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7410286289383498072L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * 时间字段id
     */
    private Integer modelTimeColumnId;

}
