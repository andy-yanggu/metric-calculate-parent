package com.yanggu.metriccalculate.calculate;


import com.yanggu.client.magiccube.pojo.DataDetailsWideTable;
import lombok.Data;

import java.util.List;

/**
 * 指标计算类
 * 包含了原子指标、衍生指标、复合指标
 */
@Data
public class MetricCalculate extends DataDetailsWideTable {

    /**
     * 原子指标计算类
     */
    private List<AtomMetricCalculate> atomMetricCalculateList;

    /**
     * 派生指标计算类
     */
    private List<DeriveMetricCalculate> deriveMetricCalculateList;

    /**
     * 复合指标计算类
     */
    private List<CompositeMetricCalculate> compositeMetricCalculateList;

}

