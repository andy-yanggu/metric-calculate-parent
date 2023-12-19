package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表时间字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelTimeColumnVO extends BaseVO implements Serializable {

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
     * 宽表字段名称
     */
    @ExcelExport(name = "宽表字段名称", sort = 2)
    @ExcelImport(name = "宽表字段名称")
    private String modelColumnName;

    /**
     * 时间格式
     */
    @ExcelExport(name = "时间格式", sort = 3)
    @ExcelImport(name = "时间格式")
    private String timeFormat;

    /**
     * 索引
     */
    @ExcelExport(name = "序号", sort = 1)
    @ExcelImport(name = "序号")
    private Integer sort;

}
