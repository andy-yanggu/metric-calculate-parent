package com.yanggu.metric_calculate.core.pojo.metric;

import com.yanggu.metric_calculate.core.pojo.store.StoreInfo;
import lombok.Data;

import java.util.List;

/**
 * 复合指标可以从多个维度进行计算
 */
@Data
public class Composite {

    /**
     * 复合指标的id
     */
    private Long id;

    /**
     * 复合指标中文名
     */
    private String displayName;

    /**
     * 复合指标名称
     */
    private String name;

    /**
     * 时间字段
     */
    private TimeColumn timeColumn;

    /**
     * 多维度计算
     */
    private List<MultiDimensionCalculate> multiDimensionCalculateList;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 指标存储相关信息
     */
    private StoreInfo storeInfo;

}
