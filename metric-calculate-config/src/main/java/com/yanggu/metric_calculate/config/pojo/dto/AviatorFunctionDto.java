package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelListColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3108156531262807116L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 唯一标识
     */
    @ExcelExport(name = "Aviator函数名称", sort = 1)
    @ExcelImport(name = "Aviator函数名称")
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
     * 是否内置: 0否, 1是
     */
    @ExcelExport(name = "是否内置", sort = 4)
    @ExcelImport(name = "是否内置")
    private Boolean isBuiltIn;

    /**
     * jar存储id
     */
    private Integer jarStoreId;

    /**
     * 不是内置的聚合函数为外置jar
     */
    private JarStoreDto jarStore;

    /**
     * Aviator函数成员变量列表
     */
    @ExcelListColumn(name = "Aviator函数成员变量")
    private List<AviatorFunctionFieldDto> aviatorFunctionFieldList;

}
