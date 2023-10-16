package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最小聚合函数抽象类单元测试类
 */
class AbstractMinAggregateFunctionTest {

    private ObjectMinAggregateFunction<Integer> minValueAggregateFunction;

    @BeforeEach
    void before() {
        minValueAggregateFunction = new ObjectMinAggregateFunction<>();
        minValueAggregateFunction.init();
    }

    @Test
    void createAccumulator() {
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        minValueAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());
    }

    @Test
    void getResult() {
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        Integer result = minValueAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MutableObj<Integer> accumulator1 = minValueAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator1);
        minValueAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minValueAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(1), merge.get());
    }
    
}

class ObjectMinAggregateFunction<T extends Comparable<T>> extends AbstractMinAggregateFunction<T, T> {

    @Override
    public void init() {
        setComparator(Comparator.naturalOrder());
    }

    @Override
    public T getResult(MutableObj<T> accumulator) {
        return accumulator.get();
    }

}
