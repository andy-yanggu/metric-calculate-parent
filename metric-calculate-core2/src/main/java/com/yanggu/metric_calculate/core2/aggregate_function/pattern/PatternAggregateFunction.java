package com.yanggu.metric_calculate.core2.aggregate_function.pattern;


import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;

import java.util.TreeMap;

public class PatternAggregateFunction implements AggregateFunction<Pair<Long, JSONObject>, TreeMap<NodePattern, TreeMap<Long, JSONObject>>, TreeMap<NodePattern, TreeMap<Long, JSONObject>>> {

    private TreeMap<NodePattern, FilterFieldProcessor> filterFieldProcessorMap;

    @Override
    public TreeMap<NodePattern, TreeMap<Long, JSONObject>> createAccumulator() {
        return new TreeMap<>();
    }

    @Override
    public TreeMap<NodePattern, TreeMap<Long, JSONObject>> add(Pair<Long, JSONObject> input,
                                                               TreeMap<NodePattern, TreeMap<Long, JSONObject>> accumulator) {
        JSONObject jsonObject = input.getValue();
        filterFieldProcessorMap.forEach((nodePattern, filterProcessor) -> {
            Boolean process = filterProcessor.process(jsonObject);
            if (Boolean.TRUE.equals(process)) {
                TreeMap<Long, JSONObject> treeMap = accumulator.computeIfAbsent(nodePattern, key -> new TreeMap<>());
                treeMap.put(input.getKey(), jsonObject);
            }
        });
        return accumulator;
    }

    @Override
    public TreeMap<NodePattern, TreeMap<Long, JSONObject>> getResult(TreeMap<NodePattern, TreeMap<Long, JSONObject>> accumulator) {
        return accumulator;
    }

}
