package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.annotation.MapType;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.enums.TimeWindowEnum;
import com.yanggu.metric_calculate.core.fieldprocess.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
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

    public MetricCube<Table, Long, M, ?> createMetricCube(DimensionSet dimensionSet, Long referenceTime) {

        MergeType annotation = mergeUnitClazz.getAnnotation(MergeType.class);

        TimeWindowEnum timeWindowEnum = annotation.timeWindowType();

        //TODO 如果是MapType类型的需要根据value的聚合类型进行判断
        //TODO 对于混合类型的, 也需要进行判断
        if (mergeUnitClazz.isAnnotationPresent(MapType.class)) {

        }
        MetricCube<Table, Long, M, ?> metricCube;
        //如果是滑动计数窗口需要使用SlidingTimeWindowMetricCube
        if (TimeWindowEnum.TIME_SLIDING_WINDOW.equals(timeWindowEnum)) {
            metricCube = new SlidingTimeWindowMetricCube();
        } else if (TimeWindowEnum.TIME_SPLIT_WINDOW.equals(timeWindowEnum)) {
            metricCube = new TimedKVMetricCube();
        } else if (TimeWindowEnum.PATTERN.equals(timeWindowEnum)) {
            metricCube = new TimedKVPatternCube();
            ((TimedKVPatternCube) metricCube).setPattern(pattern);
        } else {
            throw new RuntimeException("timeWindowEnum数据错误");
        }
        metricCube.setName(name);
        metricCube.setKey(key);
        metricCube.setTimeBaselineDimension(timeBaselineDimension);
        metricCube.setDimensionSet(dimensionSet);
        //设置数据当前聚合时间
        metricCube.setReferenceTime(referenceTime);
        metricCube.init();
        return metricCube;
    }

}
