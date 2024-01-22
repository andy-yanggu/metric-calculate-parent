package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.enums.AccuracyEnum;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelListColumn;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelObject;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 派生指标 实体类。
 */
@Data
public class DeriveDTO implements Serializable {

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
     * 原子指标id
     */
    private Integer atomId;

    /**
     * 原子指标
     */
    private AtomDTO atom;

    /**
     * 维度字段
     */
    @ExcelListColumn(name = "维度字段")
    private List<ModelDimensionColumnDTO> modelDimensionColumnList;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    @ExcelObject(name = "前置过滤条件")
    private AviatorExpressParamDTO filterExpressParam;

    /**
     * 窗口参数
     */
    @ExcelObject(name = "窗口参数")
    private WindowParamDTO windowParam;

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