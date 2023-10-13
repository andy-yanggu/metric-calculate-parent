package com.yanggu.metric_calculate.core.aggregate_function.map;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractMapAggregateFunctionTest {

    private MapAggregateFunction<String, Double, Double, Double, Map<String, Double>> mapAggregateFunction;

    @BeforeEach
    void init() {
        this.mapAggregateFunction = new MapAggregateFunction<>();
        TestSumAggregateFunction<Double> sumAggregateFunction = new TestSumAggregateFunction<>();
        this.mapAggregateFunction.setValueAggregateFunction(sumAggregateFunction);
    }

    @Test
    void createAccumulator() {
        Map<String, Double> accumulator = mapAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertTrue(accumulator instanceof HashMap);
    }

    @Test
    void add() {
        Map<String, Double> accumulator = mapAggregateFunction.createAccumulator();
        String multiFieldDistinctKey = "张三";
        AbstractMap.SimpleImmutableEntry<String, Double> tuple2 = new AbstractMap.SimpleImmutableEntry<>
                (multiFieldDistinctKey, 100.0D);
        Map<String, Double> add = mapAggregateFunction.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //应该是张三:100
        assertEquals(100.0D, add.get(multiFieldDistinctKey), 0.0D);

        add = mapAggregateFunction.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //累加后变成张三:200
        assertEquals(200.0D, add.get(multiFieldDistinctKey), 0.0D);

        String multiFieldDistinctKey2 = "李四";
        AbstractMap.SimpleImmutableEntry<String, Double> otherTuple2 = new AbstractMap.SimpleImmutableEntry<>
                (multiFieldDistinctKey2, 100.0D);
        mapAggregateFunction.add(otherTuple2, accumulator);
        assertEquals(2, accumulator.size());
        //李四100
        assertEquals(100.0D, accumulator.get(multiFieldDistinctKey2), 0.0D);
        //张三200
        assertEquals(200.0D, accumulator.get(multiFieldDistinctKey), 0.0D);

        mapAggregateFunction.add(otherTuple2, accumulator);
        //李四200
        assertEquals(200.0D, accumulator.get(multiFieldDistinctKey2), 0.0D);
        //张三200
        assertEquals(200.0D, accumulator.get(multiFieldDistinctKey), 0.0D);
    }

    @Test
    void merge() {
        Map<String, Double> accumulator = mapAggregateFunction.createAccumulator();
        Map<String, Double> accumulator1 = mapAggregateFunction.createAccumulator();
        String key = "张三";
        accumulator1.put(key, 100.0D);
        Map<String, Double> merge = mapAggregateFunction.merge(accumulator, accumulator1);
        assertNotNull(merge);
        assertSame(accumulator, merge);
        assertEquals(100.0D, merge.get(key), 0.0D);
    }

}

class MapAggregateFunction<K, V, ValueACC, ValueOUT extends Comparable<? super ValueOUT>, OUT> extends AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, OUT> {

    @Override
    public OUT getResult(Map<K, ValueACC> accumulator) {
        return null;
    }

}

class TestSumAggregateFunction<T extends Number> implements AggregateFunction<T, Double, Double> {

    @Override
    public Double createAccumulator() {
        return 0.0D;
    }

    @Override
    public Double add(T input, Double accumulator) {
        return input.doubleValue() + accumulator;
    }

    @Override
    public Double getResult(Double accumulator) {
        return accumulator;
    }

    @Override
    public Double merge(Double thisAccumulator, Double thatAccumulator) {
        return thisAccumulator + thatAccumulator;
    }

}