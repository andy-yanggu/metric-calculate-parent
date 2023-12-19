package com.yanggu.metric_calculate.config.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * CEP匹配配置数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NodePatternVO extends BaseVO implements Serializable {

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
    private AviatorExpressParamVO matchExpressParam;

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