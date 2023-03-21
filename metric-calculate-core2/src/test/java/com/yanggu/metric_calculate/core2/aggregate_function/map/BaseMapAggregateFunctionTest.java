package com.yanggu.metric_calculate.core2.aggregate_function.map;

import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import org.junit.Before;
import org.junit.Test;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BaseMapAggregateFunctionTest {

    private BaseMapAggregateFunction<MultiFieldDistinctKey, Double, Double, Double> basemap;

    @Before
    public void init() {
        this.basemap = new BaseMapAggregateFunction<>();
        SumAggregateFunction<Double> sumAggregateFunction = new SumAggregateFunction<>();
        this.basemap.setValueAggregateFunction(sumAggregateFunction);
    }

    @Test
    public void createAccumulator() {
        Map<MultiFieldDistinctKey, Double> accumulator = basemap.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
        assertTrue(accumulator instanceof HashMap);
    }

    @Test
    public void add() {
        Map<MultiFieldDistinctKey, Double> accumulator = basemap.createAccumulator();

        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey(Collections.singletonList("张三"));
        Tuple2<MultiFieldDistinctKey, Double> tuple2 = Tuples.of(multiFieldDistinctKey, 100.0D);
        Map<MultiFieldDistinctKey, Double> add = basemap.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //应该是张三:100
        assertEquals(100.0D, add.get(multiFieldDistinctKey), 0.0D);

        add = basemap.add(tuple2, accumulator);
        assertSame(add, accumulator);
        //累加后变成张三:200
        assertEquals(200.0D, add.get(multiFieldDistinctKey), 0.0D);

        MultiFieldDistinctKey multiFieldDistinctKey2 = new MultiFieldDistinctKey(Collections.singletonList("李四"));
        Tuple2<MultiFieldDistinctKey, Double> otherTuple2 = Tuples.of(multiFieldDistinctKey2, 100.0D);
        add = basemap.add(otherTuple2, accumulator);
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
    public void merge() {
        Map<MultiFieldDistinctKey, Double> accumulator = basemap.createAccumulator();
        //basemap.merge()
    }

    @Test
    public void getResult() {
    }
}