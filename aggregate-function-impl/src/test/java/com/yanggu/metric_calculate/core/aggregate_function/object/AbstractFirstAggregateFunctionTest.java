package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 首次聚合函数抽象类单元测试类
 */
class AbstractFirstAggregateFunctionTest {

    @Test
    void createAccumulator() {
        FirstAggregateFunction<String> firstObjectAggregateFunction = new FirstAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        FirstAggregateFunction<String> firstObjectAggregateFunction = new FirstAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        firstObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test1", accumulator.get());
    }

    @Test
    void getResult() {
        FirstAggregateFunction<String> firstObjectAggregateFunction = new FirstAggregateFunction<>();
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        String result = firstObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    void merge() {
        FirstAggregateFunction<String> firstObjectAggregateFunction = new FirstAggregateFunction<>();
        MutableObj<String> accumulator1 = firstObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator1);
        firstObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = firstObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test1", merge.get());
    }
    
}

class FirstAggregateFunction<T> extends AbstractFirstAggregateFunction<T> {
}
