package com.yanggu.metric_calculate.config.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CEP匹配配置数据
 */
@Data
public class NodePatternDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4872262338826165555L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * Aviator表达式参数
     */
    private AviatorExpressParamDTO matchExpressParam;

    /**
     * 间隔时间（单位毫秒值）
     */
    private Long interval;

    /**
     * 索引
     */
    private Integer sort;

    /**
     * 窗口参数id
     */
    private Integer windowParamId;

}