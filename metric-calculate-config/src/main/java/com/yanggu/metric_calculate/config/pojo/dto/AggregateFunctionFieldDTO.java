package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数的字段 实体类。
 */
@Data
public class AggregateFunctionFieldDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8681127845176848237L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 字段名称
     */
    @ExcelExport(name = "聚合函数字段名称", sort = 2)
    @ExcelImport(name = "聚合函数字段名称")
    private String name;

    /**
     * 中文名称
     */
    @ExcelExport(name = "中文名称", sort = 3)
    @ExcelImport(name = "中文名称")
    private String displayName;

    /**
     * 描述
     */
    @ExcelExport(name = "描述", sort = 4)
    @ExcelImport(name = "描述")
    private String description;


    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    /**
     * 索引
     */
    @ExcelExport(name = "索引", sort = 1)
    @ExcelImport(name = "索引")
    private Integer sort;

}