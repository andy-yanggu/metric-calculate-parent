package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelListColumn;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 聚合函数 实体类。
 */
@Data
public class AggregateFunctionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6425099649633730377L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 唯一标识
     */
    @ExcelExport(name = "聚合函数名称", sort = 1)
    @ExcelImport(name = "聚合函数名称")
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
     * 聚合函数类型（数值、集合、对象、混合、映射）
     */
    @ExcelExport(name = "聚合函数类型", sort = 4)
    @ExcelImport(name = "聚合函数类型")
    private AggregateFunctionTypeEnums type;

    /**
     * 集合型和对象型主键策略（0没有主键、1去重字段、2排序字段、3比较字段）
     */
    @ExcelExport(name = "集合型和对象型主键策略", sort = 5)
    @ExcelImport(name = "集合型和对象型主键策略")
    private Integer keyStrategy;

    /**
     * 集合型和对象型保留字段策略（0不保留任何数据、1保留指定字段、2保留原始数据）
     */
    @ExcelExport(name = "集合型和对象型保留字段策略", sort = 6)
    @ExcelImport(name = "集合型和对象型保留字段策略")
    private Integer retainStrategy;

    /**
     * 数值型是否需要多个参数（0否，1是需要多个例如协方差）
     */
    @ExcelExport(name = "数值型是否需要多个参数", sort = 7)
    @ExcelImport(name = "数值型是否需要多个参数")
    private Boolean multiNumber;

    /**
     * 是否内置: 0否, 1是
     */
    @ExcelExport(name = "是否内置", sort = 8)
    @ExcelImport(name = "是否内置")
    private Boolean isBuiltIn;

    /**
     * jar存储id
     */
    private Integer jarStoreId;

    /**
     * 不是内置的聚合函数为外置jar
     */
    private JarStoreDTO jarStore;

    /**
     * 聚合函数成员变量
     */
    @ExcelListColumn(name = "聚合函数成员变量")
    private List<AggregateFunctionFieldDTO> aggregateFunctionFieldList;

}
