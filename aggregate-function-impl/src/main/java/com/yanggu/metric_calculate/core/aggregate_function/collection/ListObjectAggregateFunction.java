package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import lombok.Data;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 对象列表
 *
 */
@Data
@Collective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "LISTOBJECT", displayName = "对象列表")
public class ListObjectAggregateFunction implements AggregateFunction<JSONObject, List<JSONObject>, List<JSONObject>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度限制")
    private Integer limit = 10;

    @Override
    public List<JSONObject> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public List<JSONObject> add(JSONObject input, List<JSONObject> accumulator) {
        if (limit > accumulator.size()) {
            accumulator.add(input);
        }
        return accumulator;
    }

    @Override
    public List<JSONObject> getResult(List<JSONObject> accumulator) {
        return accumulator;
    }

    @Override
    public List<JSONObject> merge(List<JSONObject> thisAccumulator, List<JSONObject> thatAccumulator) {
        Iterator<JSONObject> iterator = thatAccumulator.iterator();
        while (limit > thisAccumulator.size() && iterator.hasNext()) {
            thisAccumulator.add(iterator.next());
        }
        return thisAccumulator;
    }

}
