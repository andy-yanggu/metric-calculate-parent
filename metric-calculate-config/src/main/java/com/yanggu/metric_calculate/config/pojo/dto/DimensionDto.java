package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维度表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DimensionDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2963633798023385948L;

    private Integer id;

    /**
     * 维度名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 备注
     */
    private String description;

}
