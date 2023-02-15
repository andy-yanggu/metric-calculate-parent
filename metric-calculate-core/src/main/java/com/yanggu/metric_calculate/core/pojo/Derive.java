package com.yanggu.metric_calculate.core.pojo;

import com.yanggu.metric_calculate.core.enums.TimeUnit;
import lombok.Data;

import java.util.List;


/**
 * 派生指标
 */
@Data
public class Derive {

    /**
     * 派生指标id
     */
    private Long id;

    /**
     * 派生指标名称
     */
    private String name;

    /**
     * 派生指标中文名
     */
    private String displayName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 维度字段
     */
    private List<Dimension> dimension;

    /**
     * 时间字段
     */
    private TimeColumn timeColumn;

    /**
     * 前置过滤条件
     */
    private String filter;

    /**
     * 聚合逻辑
     */
    private String calculateLogic;

    /**
     * 是否是自定义udaf
     */
    private Boolean isUdaf;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParam baseUdafParam;

    /**
     * 对于滑动计数窗口和CEP类型, 需要额外的聚合处理器
     */
    private BaseUdafParam externalBaseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUnitUdafParam mapUdafParam;

    /**
     * 聚合时间长度
     */
    private Integer duration;

    /**
     * 聚合时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 存储宽表
     */
    private Store store;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

}
