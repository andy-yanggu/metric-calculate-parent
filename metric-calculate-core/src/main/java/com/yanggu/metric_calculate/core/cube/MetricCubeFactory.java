package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.enums.TimeWindowEnum;
import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.SlidingTimeWindowTable;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.pattern.EventState;
import com.yanggu.metric_calculate.core.unit.pattern.Pattern;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

@Data
public class MetricCubeFactory<M extends MergedUnit<M> & Value<?>> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 聚合类的Unit
     */
    private Class<? extends MergedUnit<?>> mergeUnitClazz;

    private Pattern pattern;

    public <E extends EventState<T, E>, T> MetricCube<Table, Long, M, ?> createMetricCube(DimensionSet dimensionSet, Long referenceTime) {
        MergeType annotation = mergeUnitClazz.getAnnotation(MergeType.class);

        TimeWindowEnum timeWindowEnum = annotation.timeWindowType();
        MetricCube<Table, Long, M, ?> metricCube;
        //如果是滑动计数窗口需要使用CountWindowMetricCube
        if (TimeWindowEnum.TIME_SLIDING_WINDOW.equals(timeWindowEnum)) {
            metricCube = new SlidingTimeWindowMetricCube();
            SlidingTimeWindowTable<M> table = new SlidingTimeWindowTable<>();
            metricCube.setTable(table);
        } else if (TimeWindowEnum.TIME_SPLIT_WINDOW.equals(timeWindowEnum)) {
            TimeSeriesKVTable<M> table = new TimeSeriesKVTable<>();
            table.setTimeBaselineDimension(timeBaselineDimension);
            metricCube = new TimedKVMetricCube();
            metricCube.setTable(table);
        } else if (TimeWindowEnum.PATTERN.equals(timeWindowEnum)) {
            metricCube = new TimedKVPatternCube();
            metricCube.setTimeBaselineDimension(timeBaselineDimension);
            ((TimedKVPatternCube) metricCube).setPattern(pattern);
            metricCube.init();
        } else {
            throw new RuntimeException("timeWindowEnum数据错误");
        }
        metricCube.setName(name);
        metricCube.setKey(key);
        metricCube.setTimeBaselineDimension(timeBaselineDimension);
        metricCube.setDimensionSet(dimensionSet);
        //设置数据当前聚合时间
        metricCube.setReferenceTime(referenceTime);
        return metricCube;
    }

}
