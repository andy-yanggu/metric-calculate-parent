package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 最小聚合函数抽象类单元测试类
 */
class AbstractMinAggregateFunctionTest {

    private TestObjectMinAggregateFunction<Integer> minValueAggregateFunction;

    @BeforeEach
    void init() {
        minValueAggregateFunction = new TestObjectMinAggregateFunction<>();
        minValueAggregateFunction.init();
    }

    @Test
    void testCreateAccumulator() {
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void testAdd() {
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        minValueAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());
    }

    @Test
    void testGetResult() {
        MutableObj<Integer> accumulator = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator);
        Integer result = minValueAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void testMerge() {
        MutableObj<Integer> accumulator1 = minValueAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = minValueAggregateFunction.createAccumulator();

        minValueAggregateFunction.add(1, accumulator1);
        minValueAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = minValueAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(1), merge.get());
    }

}

class TestObjectMinAggregateFunction<IN extends Comparable<IN>> extends AbstractMinAggregateFunction<IN, IN> {

    @Override
    public void init() {
        setComparator(Comparator.naturalOrder());
    }

    @Override
    public IN getResult(MutableObj<IN> accumulator) {
        return accumulator.get();
    }

}
