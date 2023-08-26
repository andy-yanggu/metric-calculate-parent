package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.enums.DataType;
import com.yanggu.metric_calculate.config.enums.ModelColumnFieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelColumnDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5841191173175360292L;

    private Integer id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 数据类型(STRING、BOOLEAN、LONG、DOUBLE)
     */
    private DataType dataType;

    /**
     * 描述
     */
    private String description;

    /**
     * 字段类型(REAL、VIRTUAL)
     */
    private ModelColumnFieldType fieldType;

    /**
     * 如果是虚拟字段，增加Aviator表达式
     */
    private AviatorExpressParamDto aviatorExpressParam;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 索引
     */
    private Integer sort;

}
