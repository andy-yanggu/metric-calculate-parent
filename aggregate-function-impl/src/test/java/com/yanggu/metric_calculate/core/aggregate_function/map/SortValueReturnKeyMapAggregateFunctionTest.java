package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortValueReturnKeyMapAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(SortValueReturnKeyMapAggregateFunction.class, "SORTVALUERETURNKEYMAP");
    }

    @Test
    void testMapType() {
        AggregateFunctionTestBase.testMapType(SortValueReturnKeyMapAggregateFunction.class);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(SortValueReturnKeyMapAggregateFunction.class);
    }

    @Test
    void getResult1() {
        SortValueReturnKeyMapAggregateFunction<String, Double, Double, Double> aggregateFunction = new SortValueReturnKeyMapAggregateFunction<>();
        aggregateFunction.setValueAggregateFunction(new SumAggregateFunction<>());
        //升序取4个
        aggregateFunction.setAsc(true);
        aggregateFunction.setLimit(4);

        Map<String, Double> accumulator = new HashMap<>();
        accumulator.put("test1", 0.0D);
        accumulator.put("test2", -1.0D);
        accumulator.put("test3", 23.0D);
        accumulator.put("test4", 2.0D);
        accumulator.put("test5", 1.0D);

        List<String> result = aggregateFunction.getResult(accumulator);
        assertEquals(ListUtil.of("test2", "test1", "test5", "test4"), result);
    }

}