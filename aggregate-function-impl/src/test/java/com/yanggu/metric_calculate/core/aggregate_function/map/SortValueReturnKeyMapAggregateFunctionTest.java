package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SortValueReturnKeyMapAggregateFunctionTest {

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = SortValueReturnKeyMapAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("SORTVALUERETURNKEYMAP", aggregateFunctionAnnotation.name());
    }

    @Test
    void testMapType() {
        MapType mapType = SortValueReturnKeyMapAggregateFunction.class.getAnnotation(MapType.class);
        assertNotNull(mapType);
    }

    @Test
    void testConstructor() {
        SortValueReturnKeyMapAggregateFunction<String, Double, Double, Double> aggregateFunction = new SortValueReturnKeyMapAggregateFunction<>();
        assertNotNull(aggregateFunction);
        assertNull(aggregateFunction.getValueAggregateFunction());
        assertTrue(aggregateFunction.getAsc());
        assertEquals(Integer.valueOf(10), aggregateFunction.getLimit());
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