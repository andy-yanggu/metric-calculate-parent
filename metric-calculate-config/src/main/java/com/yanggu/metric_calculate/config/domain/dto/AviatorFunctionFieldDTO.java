package com.yanggu.metric_calculate.config.domain.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Aviator函数字段模板 实体类。
 */
@Data
public class AviatorFunctionFieldDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1248441229349580401L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 字段名称
     */
    @ExcelExport(name = "Aviator函数字段名称", sort = 2)
    @ExcelImport(name = "Aviator函数字段名称")
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
     * Aviator函数id
     */
    private Integer aviatorFunctionId;

    /**
     * 索引
     */
    @ExcelExport(name = "索引", sort = 1)
    @ExcelImport(name = "索引")
    private Integer sort;

}