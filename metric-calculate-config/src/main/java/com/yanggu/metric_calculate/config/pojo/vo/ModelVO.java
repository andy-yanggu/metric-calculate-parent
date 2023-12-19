package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelListColumn;
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
public class ModelVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3720677788471627295L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 宽表名称
     */
    @ExcelExport(name = "宽表名称")
    @ExcelImport(name = "宽表名称")
    @NotBlank(message = "宽表名称不能为空")
    private String name;

    /**
     * 中文名称
     */
    @ExcelExport(name = "中文名称")
    @ExcelImport(name = "中文名称")
    @NotBlank(message = "宽表中文名称不能为空")
    private String displayName;

    /**
     * 描述
     */
    @ExcelExport(name = "描述")
    @ExcelImport(name = "描述")
    private String description;

    /**
     * 目录id
     */
    private Integer directoryId;

    /**
     * 宽表字段列表
     */
    @ExcelListColumn(name = "宽表字段")
    @NotEmpty(message = "宽表字段列表不能为空")
    private List<ModelColumnVO> modelColumnList;

    /**
     * 时间字段列表
     */
    @NotEmpty(message = "宽表时间字段列表不能为空")
    @ExcelListColumn(name = "时间字段")
    private List<ModelTimeColumnVO> modelTimeColumnList;

    /**
     * 维度字段列表
     */
    @NotEmpty(message = "宽表维度字段列表不能为空")
    @ExcelListColumn(name = "维度字段")
    private List<ModelDimensionColumnVO> modelDimensionColumnList;

    /**
     * 派生指标列表
     */
    private List<DeriveVO> deriveList;

}
