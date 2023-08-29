package com.yanggu.metric_calculate.core.pojo.data_detail_table;


import com.yanggu.metric_calculate.core.enums.BasicType;
import com.yanggu.metric_calculate.core.enums.FieldTypeEnum;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

/**
 * 宽表字段信息
 */
@Data
public class ModelColumn {

    /**
     * 字段id
     */
    private Long id;

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段中文名
     */
    private String displayName;

    /**
     * 字段数据类型
     */
    private BasicType dataType;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 字段类型
     */
    private FieldTypeEnum fieldType;

    /**
     * Aviator表达式参数
     */
    private AviatorExpressParam aviatorExpressParam;

}
