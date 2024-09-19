package com.yanggu.metric_calculate.core.aggregate_function.map;


import com.yanggu.metric_calculate.core.aggregate_function.numeric.TestSumAggregateFunction;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractMapAggregateFunctionTest {

    private TestMapAggregateFunction<String, Double, Double, Double> mapAggregateFunction;

    @BeforeEach
    void init() {
        this.mapAggregateFunction = new TestMapAggregateFunction<>();
        var sumAggregateFunction = new TestSumAggregateFunction<Double>();
        this.mapAggregateFunction.setValueAggregateFunction(sumAggregateFunction);
    }

    @Test
    void testCreateAccumulator() {
        var accumulator = mapAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertTrue(accumulator instanceof HashMap);
    }

    @Test
    void testAdd() {
        var accumulator = mapAggregateFunction.createAccumulator();
        String name1 = "张三";
        Pair<String, Double> tuple2 = new Pair<>(name1, 100.0D);
        Map<String, Double> add = mapAggregateFunction.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //应该是张三:100
        assertEquals(100.0D, add.get(name1), 0.0D);

        add = mapAggregateFunction.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //累加后变成张三:200
        assertEquals(200.0D, add.get(name1), 0.0D);

        String name2 = "李四";
        Pair<String, Double> otherTuple2 = new Pair<>(name2, 100.0D);
        mapAggregateFunction.add(otherTuple2, accumulator);
        assertEquals(2, accumulator.size());
        //李四100
        assertEquals(100.0D, accumulator.get(name2), 0.0D);
        //张三200
        assertEquals(200.0D, accumulator.get(name1), 0.0D);

        mapAggregateFunction.add(otherTuple2, accumulator);
        //李四200
        assertEquals(200.0D, accumulator.get(name2), 0.0D);
        //张三200
        assertEquals(200.0D, accumulator.get(name1), 0.0D);
    }

    @Test
    void testMerge() {
        Map<String, Double> accumulator1 = mapAggregateFunction.createAccumulator();
        Map<String, Double> accumulator2 = mapAggregateFunction.createAccumulator();
        String key = "张三";
        accumulator2.put(key, 100.0D);
        Map<String, Double> merge = mapAggregateFunction.merge(accumulator1, accumulator2);
        assertNotNull(merge);
        assertSame(accumulator1, merge);
        assertEquals(100.0D, merge.get(key), 0.0D);
    }

}

class TestMapAggregateFunction<K, V, ValueACC, ValueOUT> extends AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, Map<K, ValueOUT>> {
}
