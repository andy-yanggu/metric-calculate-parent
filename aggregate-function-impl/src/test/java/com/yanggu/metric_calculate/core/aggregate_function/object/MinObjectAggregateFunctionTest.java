package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最小对象单元测试类
 */
class MinObjectAggregateFunctionTest {

    @Test
    void testAggregateFunctionName() {
        AggregateFunctionTestBase.testAggregateFunctionName(MinObjectAggregateFunction.class, "MINOBJECT");
    }

    @Test
    void testObjective() {
        AggregateFunctionTestBase.testObjective(MinObjectAggregateFunction.class, 3, 2);
    }

    @Test
    void testNoArgsConstructor() {
        AggregateFunctionTestBase.testNoArgsConstructor(MinObjectAggregateFunction.class);
    }

    @Test
    void getResult() {
        MinObjectAggregateFunction minObjectAggregateFunction = new MinObjectAggregateFunction();

        MutableObj<Pair<MultiFieldData, Map<String, Object>>> accumulator = new MutableObj<>();
        Map<String, Object> data = new HashMap<>();
        accumulator.set(new Pair<>(null, data));
        Map<String, Object> result = minObjectAggregateFunction.getResult(accumulator);
        assertEquals(data, result);
    }

}