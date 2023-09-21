package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 基本映射类型单元测试类
 */
class BaseMapAggregateFunctionTest {

    private BaseMapAggregateFunction<String, Double, Double, Double> basemap;

    @BeforeEach
    void init() {
        this.basemap = new BaseMapAggregateFunction<>();
        SumAggregateFunction<Double> sumAggregateFunction = new SumAggregateFunction<>();
        this.basemap.setValueAggregateFunction(sumAggregateFunction);
    }

    @Test
    void testMergeType() {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = BaseMapAggregateFunction.class.getAnnotation(AggregateFunctionAnnotation.class);
        assertEquals("BASEMAP", aggregateFunctionAnnotation.name());
    }

    @Test
    void testMapType() {
        MapType mapType = BaseMapAggregateFunction.class.getAnnotation(MapType.class);
        assertNotNull(mapType);
    }

    @Test
    void testConstructor() {
        BaseMapAggregateFunction<String, Double, Double, Double> aggregateFunction = new BaseMapAggregateFunction<>();
        assertNotNull(aggregateFunction);
        assertNull(aggregateFunction.getValueAggregateFunction());
    }

    @Test
    void createAccumulator() {
        Map<String, Double> accumulator = basemap.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertTrue(accumulator instanceof HashMap);
    }

    @Test
    void add() {
        Map<String, Double> accumulator = basemap.createAccumulator();
        String multiFieldDistinctKey = "张三";
        AbstractMap.SimpleImmutableEntry<String, Double> tuple2 = new AbstractMap.SimpleImmutableEntry<>
                (multiFieldDistinctKey, 100.0D);
        Map<String, Double> add = basemap.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //应该是张三:100
        assertEquals(100.0D, add.get(multiFieldDistinctKey), 0.0D);

        add = basemap.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //累加后变成张三:200
        assertEquals(200.0D, add.get(multiFieldDistinctKey), 0.0D);

        String multiFieldDistinctKey2 = "李四";
        AbstractMap.SimpleImmutableEntry<String, Double> otherTuple2 = new AbstractMap.SimpleImmutableEntry<>
                (multiFieldDistinctKey2, 100.0D);
        basemap.add(otherTuple2, accumulator);
        assertEquals(2, accumulator.size());
        //李四100
        assertEquals(100.0D, accumulator.get(multiFieldDistinctKey2), 0.0D);
        //张三200
        assertEquals(200.0D, accumulator.get(multiFieldDistinctKey), 0.0D);

        basemap.add(otherTuple2, accumulator);
        //李四200
        assertEquals(200.0D, accumulator.get(multiFieldDistinctKey2), 0.0D);
        //张三200
        assertEquals(200.0D, accumulator.get(multiFieldDistinctKey), 0.0D);
    }

    @Test
    void merge() {
        Map<String, Double> accumulator = basemap.createAccumulator();
        Map<String, Double> accumulator1 = basemap.createAccumulator();
        String key = "张三";
        accumulator1.put(key, 100.0D);
        Map<String, Double> merge = basemap.merge(accumulator, accumulator1);
        assertNotNull(merge);
        assertSame(accumulator, merge);
        assertEquals(100.0D, merge.get(key), 0.0D);
    }

    @Test
    void getResult() {
        Map<String, Double> accumulator = basemap.createAccumulator();
        String key = "张三";
        Map<String, Double> add = basemap.add(new AbstractMap.SimpleImmutableEntry<>(key, 100.0D), accumulator);

        Map<String, Double> result = basemap.getResult(add);
        assertNotNull(result);
        assertEquals(100.0D, result.get(key), 0.0D);
    }

}