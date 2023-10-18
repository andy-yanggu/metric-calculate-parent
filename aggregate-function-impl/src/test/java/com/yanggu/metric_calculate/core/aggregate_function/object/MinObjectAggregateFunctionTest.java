package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

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

        MutableObj<KeyValue<MultiFieldData, JSONObject>> accumulator = new MutableObj<>();
        JSONObject data = new JSONObject();
        accumulator.set(new KeyValue<>(null, data));
        JSONObject result = minObjectAggregateFunction.getResult(accumulator);
        assertEquals(data, result);
    }

}