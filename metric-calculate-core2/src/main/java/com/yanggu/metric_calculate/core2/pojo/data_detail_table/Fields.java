package com.yanggu.metric_calculate.core2.pojo.data_detail_table;


import com.yanggu.metric_calculate.core2.enums.BasicType;
import com.yanggu.metric_calculate.core2.enums.FieldTypeEnum;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

/**
 * 宽表字段信息
 */
@Data
public class Fields {

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
    private BasicType valueType;

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
    private AviatorExpressParam expressParam;

}
