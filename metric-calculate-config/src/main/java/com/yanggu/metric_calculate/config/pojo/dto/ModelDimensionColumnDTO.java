package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维度字段 实体类。
 */
@Data
public class ModelDimensionColumnDTO implements Serializable {

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
     * 宽表字段名称
     */
    @ExcelExport(name = "宽表字段名称", sort = 2)
    @ExcelImport(name = "宽表字段名称")
    private String modelColumnName;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度名称
     */
    @ExcelExport(name = "维度名称", sort = 3)
    @ExcelImport(name = "维度名称")
    private String dimensionName;

    /**
     * 索引
     */
    @ExcelExport(name = "序号", sort = 1)
    @ExcelImport(name = "序号")
    private Integer sort;

}
