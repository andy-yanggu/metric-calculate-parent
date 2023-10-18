package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最大对象单元测试类
 */
class MaxObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MaxObjectAggregateFunction.class, "MAXOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MaxObjectAggregateFunction.class, 3, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MaxObjectAggregateFunction.class);
    }

    @Test
    void getResult() {
        var maxObjectAggregateFunction = new MaxObjectAggregateFunction();
        var accumulator = maxObjectAggregateFunction.createAccumulator();
        JSONObject data = new JSONObject();
        accumulator.set(new Pair<>(null, data));
        var result = maxObjectAggregateFunction.getResult(accumulator);
        assertEquals(data, result);
    }

}