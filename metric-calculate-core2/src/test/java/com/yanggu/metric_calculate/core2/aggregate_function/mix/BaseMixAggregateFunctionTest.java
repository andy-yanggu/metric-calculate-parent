package com.yanggu.metric_calculate.core2.aggregate_function.mix;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BaseMixAggregateFunctionTest {

    private BaseMixAggregateFunction baseMixAggregateFunction;

    @Before
    public void init() {
        baseMixAggregateFunction = new BaseMixAggregateFunction();
        Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
        mixAggregateFunctionMap.put("test1", new SumAggregateFunction<>());
        mixAggregateFunctionMap.put("test2", new SumAggregateFunction<>());
        baseMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);

        String express =
                "if (tes1 == nil) {\n" +
                "   tes1 = 0;\n" +
                "}\n" +
                "if (test2 == nil) {\n" +
                "   test2 = 0;\n" +
                "}\n" +
                "return test1 + test2;";
        Expression expression = AviatorEvaluator.compile(express, true);
        baseMixAggregateFunction.setExpression(expression);
    }

    @Test
    public void createAccumulator() {
        Map<String, Object> accumulator = baseMixAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    public void add() {
        Map<String, Object> accumulator = baseMixAggregateFunction.createAccumulator();
        Map<String, Object> input = new HashMap<>();

        input.put("test1", 1);
        baseMixAggregateFunction.add(input, accumulator);
        assertEquals(1, accumulator.size());
        assertEquals(1.0D, ((Double) accumulator.get("test1")), 0.0D);

        input.clear();
        input.put("test1", 3);
        baseMixAggregateFunction.add(input, accumulator);
        assertEquals(1, accumulator.size());
        assertEquals(4.0D, ((Double) accumulator.get("test1")), 0.0D);

        input.clear();
        input.put("test2", 3);
        baseMixAggregateFunction.add(input, accumulator);
        assertEquals(2, accumulator.size());
        assertEquals(4.0D, ((Double) accumulator.get("test1")), 0.0D);
        assertEquals(3.0D, ((Double) accumulator.get("test2")), 0.0D);
    }

    @Test
    public void getResult() {
        Map<String, Object> accumulator = baseMixAggregateFunction.createAccumulator();
        accumulator.put("test1", 1.0D);
        accumulator.put("test2", 2.0D);
        Object result = baseMixAggregateFunction.getResult(accumulator);
        assertEquals(3.0D, ((Double) result), 0.0D);
    }

    @Test
    public void merge() {
        Map<String, Object> accumulator = baseMixAggregateFunction.createAccumulator();
        accumulator.put("test1", 1.0D);
        accumulator.put("test2", 2.0D);

        Map<String, Object> accumulator2 = baseMixAggregateFunction.createAccumulator();
        accumulator2.put("test1", 1.0D);
        accumulator2.put("test2", 2.0D);
        Map<String, Object> merge = baseMixAggregateFunction.merge(accumulator, accumulator2);

        Map<String, Object> map = new HashMap<>();
        map.put("test1", 2.0D);
        map.put("test2", 4.0D);
        assertEquals(map, merge);
    }

}