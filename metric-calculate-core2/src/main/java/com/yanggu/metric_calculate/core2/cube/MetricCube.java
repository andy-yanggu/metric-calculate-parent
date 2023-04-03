package com.yanggu.metric_calculate.core2.cube;

import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.table.TimeTable;
import lombok.Data;

/**
 * 指标数据
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class MetricCube<IN, ACC, OUT> {

    /**
     * 指标的维度
     */
    private DimensionSet dimensionSet;

    /**
     * 指标数据
     */
    private TimeTable<IN, ACC, OUT> timeTable;

}
