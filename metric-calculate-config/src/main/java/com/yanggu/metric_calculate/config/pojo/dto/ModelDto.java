package com.yanggu.metric_calculate.config.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotBlank(message = "宽表名称不能为空")
    private String name;

    /**
     * 中文名称
     */
    @NotBlank(message = "宽表中文名称不能为空")
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
    @NotEmpty(message = "宽表字段列表不能为空")
    private List<ModelColumnDto> modelColumnList;

    /**
     * 时间字段列表
     */
    @NotEmpty(message = "宽表时间字段列表不能为空")
    private List<ModelTimeColumnDto> modelTimeColumnList;

    /**
     * 维度字段列表
     */
    @NotEmpty(message = "宽表维度字段列表不能为空")
    private List<ModelDimensionColumnDto> modelDimensionColumnList;

    /**
     * 派生指标列表
     */
    private List<DeriveDto> deriveList;

}
