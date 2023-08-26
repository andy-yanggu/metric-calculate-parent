package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维度字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelDimensionColumnDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3720677788471627294L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 宽表字段id
     */
    private Integer modelColumnId;

    /**
     * 宽表字段
     */
    private ModelColumnDto modelColumn;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度
     */
    private DimensionDto dimension;

    /**
     * 索引
     */
    private Integer sort;

}
