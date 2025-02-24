package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 最大聚合函数抽象类单元测试类
 */
@DisplayName("最大聚合函数抽象类单元测试类")
class AbstractMaxAggregateFunctionTest {

    private TestMaxAggregateFunction<Integer> maxObjectAggregateFunction;

    @BeforeEach
    void init() {
        maxObjectAggregateFunction = new TestMaxAggregateFunction<>();
        maxObjectAggregateFunction.init();
    }

    @Test
    void testCreateAccumulator() {
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void testAdd() {
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        assertEquals(Integer.valueOf(1), accumulator.get());

        maxObjectAggregateFunction.add(2, accumulator);
        assertEquals(Integer.valueOf(2), accumulator.get());
    }

    @Test
    void testGetResult() {
        MutableObj<Integer> accumulator = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator);
        Integer result = maxObjectAggregateFunction.getResult(accumulator);

        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    void testMerge() {
        MutableObj<Integer> accumulator1 = maxObjectAggregateFunction.createAccumulator();
        MutableObj<Integer> accumulator2 = maxObjectAggregateFunction.createAccumulator();

        maxObjectAggregateFunction.add(1, accumulator1);
        maxObjectAggregateFunction.add(2, accumulator2);

        MutableObj<Integer> merge = maxObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(Integer.valueOf(2), merge.get());
    }
    
}

class TestMaxAggregateFunction<IN extends Comparable<IN>> extends AbstractMaxAggregateFunction<IN, IN> {

    @Override
    public void init() {
        setComparator(Comparator.naturalOrder());
    }

    @Override
    public IN getResult(MutableObj<IN> accumulator) {
        return accumulator.get();
    }

}
