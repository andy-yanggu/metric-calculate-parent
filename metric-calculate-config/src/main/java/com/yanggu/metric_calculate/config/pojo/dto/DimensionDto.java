package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
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

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 维度名称
     */
    @ExcelExport(name = "维度名称")
    @ExcelImport(name = "维度名称")
    private String name;

    /**
     * 中文名称
     */
    @ExcelExport(name = "中文名称")
    @ExcelImport(name = "中文名称")
    private String displayName;

    /**
     * 描述
     */
    @ExcelExport(name = "描述")
    @ExcelImport(name = "描述")
    private String description;

}
