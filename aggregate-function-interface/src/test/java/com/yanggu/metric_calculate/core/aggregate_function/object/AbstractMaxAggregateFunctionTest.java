package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大聚合函数抽象类单元测试类
 */
class AbstractMaxAggregateFunctionTest {

    private TestMaxAggregateFunction<Integer> maxObjectAggregateFunction;

    @BeforeEach
    void before() {
        maxObjectAggregateFunction = new TestMaxAggregateFunction<>();
        maxObjectAggregateFunction.init();
    }

    @Test
    void createAccumulator() {
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxObjectAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());
    }

    @Test
    void getResult() {
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        Integer result = maxObjectAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void merge() {
        MutableObj<Integer> accumulator1 = maxObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator1);
        maxObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }
    
}

class TestMaxAggregateFunction<T extends Comparable<T>> extends AbstractMaxAggregateFunction<T, T> {

    @Override
    public void init() {
        setComparator(Comparator.naturalOrder());
    }

    @Override
    public T getResult(MutableObj<T> accumulator) {
        return accumulator.get();
    }

}
