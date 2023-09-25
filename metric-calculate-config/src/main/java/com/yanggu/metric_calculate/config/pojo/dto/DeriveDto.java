package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.enums.AccuracyEnum;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelListColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 派生指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeriveDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2005906604838021242L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 名称
     */
    @ExcelExport(name = "派生指标名称", sort = 1)
    @ExcelImport(name = "派生指标名称")
    private String name;

    /**
     * 中文名称
     */
    @ExcelExport(name = "中文名称", sort = 2)
    @ExcelImport(name = "中文名称")
    private String displayName;

    /**
     * 描述
     */
    @ExcelExport(name = "描述", sort = 3)
    @ExcelImport(name = "描述")
    private String description;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 宽表名称
     */
    @ExcelExport(name = "宽表名称", sort = 4)
    @ExcelImport(name = "宽表名称")
    private String modelName;

    /**
     * 维度字段
     */
    @ExcelListColumn(name = "维度字段")
    private List<ModelDimensionColumnDto> modelDimensionColumnList;

    /**
     * 时间字段
     */
    //@ExcelObject(name = "时间字段", nameList = {""})
    private ModelTimeColumnDto modelTimeColumn;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    private AviatorExpressParamDto filterExpressParam;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParamDto aggregateFunctionParam;

    /**
     * 窗口相关参数
     */
    private WindowParamDto windowParam;

    /**
     * 是否包含当前笔
     */
    @ExcelExport(name = "是否包含当前笔", sort = 5)
    @ExcelImport(name = "是否包含当前笔")
    private Boolean includeCurrent;

    /**
     * 计量单位
     */
    @ExcelExport(name = "计量单位", sort = 6)
    @ExcelImport(name = "计量单位")
    private String unitMeasure;

    /**
     * 精度类型(0不处理 1四舍五入 2向上保留)
     */
    @ExcelExport(name = "精度类型(0不处理 1四舍五入 2向上保留)", sort = 7)
    @ExcelImport(name = "精度类型(0不处理 1四舍五入 2向上保留)")
    private AccuracyEnum roundAccuracyType;

    /**
     * 精度长度
     */
    @ExcelExport(name = "精度长度", sort = 8)
    @ExcelImport(name = "精度长度")
    private Integer roundAccuracyLength;

    /**
     * 数据类型
     */
    @ExcelExport(name = "数据类型", sort = 9)
    @ExcelImport(name = "数据类型")
    private Integer dataType;

    /**
     * 目录编码
     */
    private String directoryCode;

}