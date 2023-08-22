package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.enums.DataType;
import com.yanggu.metric_calculate.config.enums.ModelColumnFieldType;
import lombok.Data;

import java.util.Date;

/**
 * 宽表字段
 */
@Data
public class ModelColumnDto {

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
     * 用户id
     */
    private Integer userId;

    /**
     * 索引
     */
    private Integer sort;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
