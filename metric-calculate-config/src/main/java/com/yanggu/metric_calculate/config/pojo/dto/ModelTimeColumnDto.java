package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表时间字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelTimeColumnDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 7617995233100366213L;

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
     * 中文名称
     */
    private String timeFormat;

    /**
     * 索引
     */
    private Integer sort;

}
