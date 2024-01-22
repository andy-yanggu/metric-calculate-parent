package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字段排序配置类 实体类。
 */
@Data
public class FieldOrderParamDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2165379269343044214L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 表达式id
     */
    private Integer aviatorExpressParamId;

    private AviatorExpressParamDTO aviatorExpressParam;

    /**
     * 是否升序, true升序, false降序
     */
    private Boolean isAsc;

}