package com.yanggu.metric_calculate.function_test.aggregate_function_test;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import lombok.Data;

/**
 * 测试ACC能够序列化和反序列化
 *
 */
@Data
@AggregateFunctionAnnotation(name = "TEST_ACC", displayName = "测试ACC序列化和反序列化")
@Collective(keyStrategy = 0, retainStrategy = 2)
public class TestAggSerialFunction implements AggregateFunction<String, TestAgg, TestAgg> {

    @Override
    public TestAgg createAccumulator() {
        TestAgg testAgg = new TestAgg();
        testAgg.setF1("test1");
        testAgg.setF2(100);
        return testAgg;
    }

    @Override
    public TestAgg add(String input, TestAgg accumulator) {
        return null;
    }

    @Override
    public TestAgg getResult(TestAgg accumulator) {
        return null;
    }

    @Override
    public TestAgg merge(TestAgg thisAccumulator, TestAgg thatAccumulator) {
        return null;
    }

}
