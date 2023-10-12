package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大聚合函数抽象类单元测试类
 */
class AbstractMaxAggregateFunctionTest {

    @Test
    void createAccumulator() {
        MaxAggregateFunction<Integer> maxObjectAggregateFunction = new MaxAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MaxAggregateFunction<Integer> maxObjectAggregateFunction = new MaxAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxObjectAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());
    }

    @Test
    void getResult() {
        MaxAggregateFunction<Integer> maxObjectAggregateFunction = new MaxAggregateFunction<>();
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        Integer result = maxObjectAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MaxAggregateFunction<Integer> maxObjectAggregateFunction = new MaxAggregateFunction<>();
        MutableObj<Integer> accumulator1 = maxObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator1);
        maxObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }
    
}

class MaxAggregateFunction<T extends Comparable<T>> extends AbstractMaxAggregateFunction<T, T> {

    @Override
    public T getResult(MutableObj<T> accumulator) {
        return accumulator.get();
    }

}
