package com.yanggu.metric_calculate.config.domain.vo;

import com.yanggu.metric_calculate.config.base.domain.vo.BaseVO;
import com.yanggu.metric_calculate.config.enums.AccuracyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 派生指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeriveVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2005906604838021242L;

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
     * 原子指标id
     */
    private Integer atomId;

    /**
     * 原子指标
     */
    private AtomVO atom;

    /**
     * 维度字段
     */
    private List<ModelDimensionColumnVO> modelDimensionColumnList;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    private AviatorExpressParamVO filterExpressParam;

    /**
     * 窗口参数
     */
    private WindowParamVO windowParam;

    /**
     * 是否包含当前笔
     */
    private Boolean includeCurrent;

    /**
     * 计量单位
     */
    private String unitMeasure;

    /**
     * 精度类型(0不处理 1四舍五入 2向上保留)
     */
    private AccuracyEnum roundAccuracyType;

    /**
     * 精度长度
     */
    private Integer roundAccuracyLength;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 目录编码
     */
    private String directoryCode;

}