package com.yanggu.metric_calculate.core2.cube;

import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.table.TimeTable;
import lombok.Data;

@Data
public class MetricCube<IN, ACC, OUT> {

    private DimensionSet dimensionSet;

    private TimeTable<IN, ACC, OUT> timeTable;

}
