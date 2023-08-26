package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维度字段选项 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeriveModelDimensionColumnRelationDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5533781924640114353L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * 维度字段id
     */
    private Integer dimensionColumnId;

}
