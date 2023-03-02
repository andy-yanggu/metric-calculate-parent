package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.annotation.MapType;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.enums.TimeWindowEnum;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.pojo.metric.Derive;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
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

    private Derive derive;

    public MetricCube<Table, Long, M, ?> createMetricCube(DimensionSet dimensionSet, Long timestamp) {

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
            metricCube = new PatternMetricCube();
            ((PatternMetricCube) metricCube).setNodePatternList(derive.getChainPattern().getNodePatternList());
        } else {
            throw new RuntimeException("timeWindowEnum数据错误");
        }
        metricCube.setKey(key);
        metricCube.setName(name);
        metricCube.setTimeBaselineDimension(timeBaselineDimension);
        metricCube.setDimensionSet(dimensionSet);
        //当前数据的聚合时间戳
        Long referenceTime = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        metricCube.setReferenceTime(referenceTime);
        metricCube.init();
        return metricCube;
    }

}
