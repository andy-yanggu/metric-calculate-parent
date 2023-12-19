package com.yanggu.metric_calculate.config.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Aviator函数实例 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionInstanceVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 522796813713273071L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * Aviator函数id
     */
    private Integer aviatorFunctionId;

    /**
     * Aviator函数参数的JSON数据
     */
    private Map<String, Object> param;

    /**
     * 对应的AggregateFunction
     */
    private AviatorFunctionVO aviatorFunction;

}