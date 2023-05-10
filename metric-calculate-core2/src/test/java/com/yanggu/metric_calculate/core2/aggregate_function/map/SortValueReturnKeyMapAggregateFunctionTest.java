package com.yanggu.metric_calculate.core2.aggregate_function.map;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MapType;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SortValueReturnKeyMapAggregateFunctionTest {

    @Test
    public void testMergeType() {
        MergeType mergeType = SortValueReturnKeyMapAggregateFunction.class.getAnnotation(MergeType.class);
        assertEquals("SORTVALUERETURNKEYMAP", mergeType.value());
    }

    @Test
    public void testMapType() {
        MapType mapType = SortValueReturnKeyMapAggregateFunction.class.getAnnotation(MapType.class);
        assertNotNull(mapType);
    }

    @Test
    public void testConstructor() {
        SortValueReturnKeyMapAggregateFunction<MultiFieldDistinctKey, Double, Double, Double> aggregateFunction = new SortValueReturnKeyMapAggregateFunction<>();
        assertNotNull(aggregateFunction);
        assertNull(aggregateFunction.getValueAggregateFunction());
        assertTrue(aggregateFunction.getAsc());
        assertEquals(new Integer(10), aggregateFunction.getLimit());
    }

    @Test
    public void getResult1() {
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
        assertEquals(CollUtil.toList("test2", "test1", "test5", "test4"), result);
    }

}