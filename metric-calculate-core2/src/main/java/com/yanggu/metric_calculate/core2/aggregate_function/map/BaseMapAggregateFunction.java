package com.yanggu.metric_calculate.core2.aggregate_function.map;

import com.yanggu.metric_calculate.core2.annotation.MapType;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

import java.util.HashMap;
import java.util.Map;

@MapType
@MergeType("BASEMAP")
public class BaseMapAggregateFunction<IN, ACC, OUT> extends AbstractMapAggregateFunction<IN, ACC, OUT> {

    @Override
    public OUT getResult(Map<Object, ACC> accumulator) {
        Map<Object, OUT> returnMap = new HashMap<>();
        accumulator.forEach((tempKey, tempAcc) -> returnMap.put(tempKey, valueAggregateFunction.getResult(tempAcc)));
        return (OUT) returnMap;
    }

}
