package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 宽表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3720677788471627295L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 宽表名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * 目录id
     */
    private Integer directoryId;

    /**
     * 宽表字段列表
     */
    private List<ModelColumnDto> modelColumnList;

    /**
     * 时间字段列表
     */
    private List<ModelTimeColumnDto> modelTimeColumnList;

    /**
     * 维度字段列表
     */
    private List<ModelDimensionColumnDto> modelDimensionColumnList;

}
