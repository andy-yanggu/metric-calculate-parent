package com.yanggu.metric_calculate.core2.aggregate_function.numeric;


import org.junit.Test;

/**
 * SUM求和单元测试类
 */
public class SumAggregateFunctionTest {

    @Test
    public void test1() {
        SumAggregateFunction<Integer> sumAggregateFunction = new SumAggregateFunction<>();
        Double accumulator = sumAggregateFunction.createAccumulator();
        accumulator = sumAggregateFunction.add(1, accumulator);
        Double result = sumAggregateFunction.getResult(accumulator);
        System.out.println(result);
    }

}
