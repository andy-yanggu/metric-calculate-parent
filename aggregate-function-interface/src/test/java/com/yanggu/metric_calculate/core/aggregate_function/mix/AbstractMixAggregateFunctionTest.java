package com.yanggu.metric_calculate.core.aggregate_function.mix;


import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.numeric.TestSumAggregateFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 混合型聚合函数单元测试类
 */
class AbstractMixAggregateFunctionTest {

    private TestMixAggregateFunction<Double> baseMixAggregateFunction;

    @BeforeEach
    void init() {
        baseMixAggregateFunction = new TestMixAggregateFunction<>();
        Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
        mixAggregateFunctionMap.put("test1", new TestSumAggregateFunction<>());
        mixAggregateFunctionMap.put("test2", new TestSumAggregateFunction<>());
        baseMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);

        String express =
                """
                        if (tes1 == nil) {
                           tes1 = 0;
                        }
                        if (test2 == nil) {
                           test2 = 0;
                        }
                        return test1 + test2;
                        """;
        Expression expression = AviatorEvaluator.compile(express, true);
        baseMixAggregateFunction.setExpression(expression);
    }

    @Test
    void testCreateAccumulator() {
        Map<String, Object> accumulator = baseMixAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertInstanceOf(HashMap.class, accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    void testAdd() {
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
    void testGetResult() {
        Map<String, Object> accumulator = baseMixAggregateFunction.createAccumulator();
        accumulator.put("test1", 1.0D);
        accumulator.put("test2", 2.0D);
        Double result = baseMixAggregateFunction.getResult(accumulator);
        System.out.println(result);
        assertEquals(3.0D, result, 0.0D);
    }

    @Test
    void testMerge() {
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

class TestMixAggregateFunction<OUT> extends AbstractMixAggregateFunction<OUT> {
}
