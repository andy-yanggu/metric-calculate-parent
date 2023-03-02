package com.yanggu.metric_calculate.core.cube;


import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.table.PatternTable;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.List;
import java.util.TreeMap;

/**
 *
 * @param <T> 输入的明细数据类型
 */
@Data
public class PatternMetricCube<T extends Clone<T>> implements
        MetricCube<PatternTable<T>, Long, MatchState<TreeMap<NodePattern, CloneWrapper<T>>>, PatternMetricCube<T>> {

    private static final String PREFIX = "MC.S.PATTERN.C";

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 当前数据聚合时间戳
     */
    private long referenceTime;

    /**
     * 指标维度
     */
    private DimensionSet dimensionSet;

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 事件模式匹配配置数据
     */
    private List<NodePattern> nodePatternList;

    /**
     * 时间序列表
     */
    private PatternTable<T> table;

    @Override
    public PatternMetricCube<T> init() {

        PatternTable<T> patternTable = new PatternTable<>();
        //dataMap进行默认初始化
        TreeMap<NodePattern, TimeSeriesKVTable<MatchState<T>>> dataMap = new TreeMap<>();
        nodePatternList.forEach(tempPattern -> dataMap.put(tempPattern, new TimeSeriesKVTable<>(timeBaselineDimension)));
        patternTable.setDataMap(dataMap);

        this.table = patternTable;
        return this;
    }

    @Override
    public void put(Long key, MatchState<TreeMap<NodePattern, CloneWrapper<T>>> value) {
        this.table.putValue(key, null, value);
    }

    @Override
    public PatternMetricCube<T> merge(PatternMetricCube<T> that) {
        this.table.merge(that.table);
        return this;
    }

    @Override
    public Value<?> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return this.table.query(from, fromInclusive, to, toInclusive);
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public PatternMetricCube<T> cloneEmpty() {
        PatternMetricCube<T> metricCube = new PatternMetricCube<>();
        metricCube.setKey(this.key);
        metricCube.setName(this.name);
        metricCube.setDimensionSet(this.dimensionSet);
        metricCube.setTimeBaselineDimension(this.timeBaselineDimension);
        metricCube.setNodePatternList(this.nodePatternList);
        metricCube.init();
        return metricCube;
    }

    @Override
    public PatternMetricCube<T> fastClone() {
        PatternMetricCube<T> patternMetricCube = cloneEmpty();
        patternMetricCube.setTable(this.table.fastClone());
        return patternMetricCube;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String getRealKey() {
        return getPrefix() + ":" + dimensionSet.realKey();
    }

    @Override
    public int eliminateExpiredData() {
        return 0;
    }

}
