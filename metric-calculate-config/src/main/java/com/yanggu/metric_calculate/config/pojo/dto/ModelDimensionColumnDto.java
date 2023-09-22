package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelObject;
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
    @ExcelObject(name = "宽表字段", nameList = {"name"})
    private ModelColumnDto modelColumn;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度
     */
    @ExcelObject(name = "维度", nameList = {"name"})
    private DimensionDto dimension;

    /**
     * 索引
     */
    @ExcelExport(name = "序号", sort = 1)
    @ExcelImport(name = "序号")
    private Integer sort;

}
