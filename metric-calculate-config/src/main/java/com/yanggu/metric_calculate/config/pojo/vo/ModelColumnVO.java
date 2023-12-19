package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.enums.DataType;
import com.yanggu.metric_calculate.config.enums.ModelColumnFieldType;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelColumnVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5841191173175360292L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 字段名称
     */
    @ExcelExport(name = "字段名称", sort = 2)
    @ExcelImport(name = "字段名称")
    private String name;

    /**
     * 中文名称
     */
    @ExcelExport(name = "中文名称", sort = 3)
    @ExcelImport(name = "中文名称")
    private String displayName;

    /**
     * 数据类型(STRING、BOOLEAN、LONG、DOUBLE)
     */
    @ExcelExport(name = "数据类型", sort = 4)
    @ExcelImport(name = "数据类型")
    private DataType dataType;

    /**
     * 描述
     */
    @ExcelExport(name = "描述", sort = 5)
    @ExcelImport(name = "描述")
    private String description;

    /**
     * 字段类型(REAL、VIRTUAL)
     */
    @ExcelExport(name = "字段类型", sort = 6)
    @ExcelImport(name = "字段类型")
    private ModelColumnFieldType fieldType;

    /**
     * 如果是虚拟字段，增加Aviator表达式
     */
    @ExcelObject(name = "Aviator表达式")
    private AviatorExpressParamVO aviatorExpressParam;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 索引
     */
    @ExcelExport(name = "序号", sort = 1)
    @ExcelImport(name = "序号")
    private Integer sort;

}
