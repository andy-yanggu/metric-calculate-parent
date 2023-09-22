package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelObject;
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
    @ExcelObject(name = "宽表字段", nameList = {"name"})
    private ModelColumnDto modelColumn;

    /**
     * 时间格式
     */
    @ExcelExport(name = "时间格式", sort = 2)
    @ExcelImport(name = "时间格式")
    private String timeFormat;

    /**
     * 索引
     */
    @ExcelExport(name = "序号", sort = 1)
    @ExcelImport(name = "序号")
    private Integer sort;

}
