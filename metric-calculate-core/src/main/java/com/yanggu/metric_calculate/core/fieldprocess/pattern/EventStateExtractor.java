package com.yanggu.metric_calculate.core.fieldprocess.pattern;

import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.BaseAggregateFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.ChainPattern;
import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.*;
import lombok.Data;

import java.util.*;

@Data
public class EventStateExtractor<T, M extends MergedUnit<M>>
        implements AggregateFieldProcessor<T, M> {

    private ChainPattern chainPattern;

    private BaseUdafParam baseUdafParam;

    private UnitFactory unitFactory;

    private Map<String, Class<?>> fieldMap;

    private TreeMap<NodePattern, FilterFieldProcessor<T>> filterFieldProcessorMap;

    /**
     * 需要进行二次聚合计算
     * <p>例如滑动计数窗口函数, 最近5次, 求平均值</p>
     * <p>CEP, 按照最后一条数据进行聚合计算</p>
     */
    private BaseAggregateFieldProcessor<T, ?> externalAggregateFieldProcessor;

    @Override
    public void init() throws Exception {
        TreeMap<NodePattern, FilterFieldProcessor<T>> tempFilterFieldProcessorMap = new TreeMap<>();

        List<NodePattern> nodePatternList = chainPattern.getNodePatternList();
        for (NodePattern node : nodePatternList) {
            FilterFieldProcessor<T> filterFieldProcessor =
                    FieldProcessorUtil.getFilterFieldProcessor(fieldMap, node.getMatchExpress());
            tempFilterFieldProcessorMap.put(node, filterFieldProcessor);
        }
        this.filterFieldProcessorMap = tempFilterFieldProcessorMap;

        this.externalAggregateFieldProcessor = FieldProcessorUtil
                .getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

    }

    @Override
    public M process(T event) {
        TreeMap<NodePattern, CloneWrapper<T>> dataMap = new TreeMap<>();
        filterFieldProcessorMap.forEach((nodePattern, filterProcessor) -> {
            Boolean process = filterProcessor.process(event);
            if (process.equals(true)) {
                dataMap.put(nodePattern, CloneWrapper.wrap(event));
            }
        });

        return ((M) new MatchState<>(dataMap));
    }

    @Override
    public String getAggregateType() {
        return null;
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return null;
    }

    @Override
    public Object callBack(Object input) {
        List<T> tempValueList = new ArrayList<>(((TreeMap<Long, T>) input).values());
        MergedUnit<?> mergedUnit = tempValueList.stream()
                .map(tempValue -> externalAggregateFieldProcessor.process(tempValue))
                .reduce(MergedUnit::merge)
                .orElseThrow(() -> new RuntimeException("MergeUnit的merge方法执行失败"));
        return ValueMapper.value(((Value<?>) mergedUnit));
    }

}