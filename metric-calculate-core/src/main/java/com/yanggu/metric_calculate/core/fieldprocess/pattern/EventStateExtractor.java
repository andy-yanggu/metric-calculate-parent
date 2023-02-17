package com.yanggu.metric_calculate.core.fieldprocess.pattern;

import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EventStateExtractor<T> implements AggregateFieldProcessor<T, MatchState<Map<String, T>>> {

    private UnitFactory unitFactory;

    private ChainPattern chainPattern;

    private Map<String, Class<?>> fieldMap;

    private Map<String, FilterFieldProcessor<T>> filterFieldProcessorMap;

    @Override
    public void init() throws Exception {
        List<ChainPattern.Node> nodes = chainPattern.getNodes();
        Map<String, FilterFieldProcessor<T>> tempFilterFieldProcessorMap = new HashMap<>();
        for (ChainPattern.Node node : nodes) {
            String name = node.getName();
            FilterFieldProcessor<T> filterFieldProcessor =
                    FieldProcessorUtil.getFilterFieldProcessor(fieldMap, node.getMatchExpress());
            tempFilterFieldProcessorMap.put(name, filterFieldProcessor);
        }
        this.filterFieldProcessorMap = tempFilterFieldProcessorMap;
    }

    @Override
    public MatchState<Map<String, T>> process(T event) {
        Map<String, T> dataMap = new HashMap<>();
        filterFieldProcessorMap.forEach((nodeName, filterProcessor) -> {
            Boolean process = filterProcessor.process(event);
            if (process.equals(true)) {
                dataMap.put(nodeName, event);
            }
        });

        return new MatchState<>(dataMap);
    }

    @Override
    public String getAggregateType() {
        return null;
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return null;
    }

}