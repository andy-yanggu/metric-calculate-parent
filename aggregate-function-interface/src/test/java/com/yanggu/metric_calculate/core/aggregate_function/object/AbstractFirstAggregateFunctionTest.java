package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 首次聚合函数抽象类单元测试类
 */
@DisplayName("首次聚合函数抽象类单元测试类")
class AbstractFirstAggregateFunctionTest {

    private TestFirstAggregateFunction<String> firstObjectAggregateFunction;

    @BeforeEach
    void init() {
        firstObjectAggregateFunction = new TestFirstAggregateFunction<>();
    }

    @Test
    void testCreateAccumulator() {
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void testAdd() {
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();
        firstObjectAggregateFunction.add(null, accumulator);
        assertNull(accumulator.get());

        firstObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        firstObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test1", accumulator.get());
    }

    @Test
    void testGetResult() {
        MutableObj<String> accumulator = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator);
        String result = firstObjectAggregateFunction.getResult(accumulator);
        assertEquals("test1", result);
    }

    @Test
    void testMerge() {
        MutableObj<String> accumulator1 = firstObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = firstObjectAggregateFunction.createAccumulator();

        firstObjectAggregateFunction.add("test1", accumulator1);
        firstObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = firstObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test1", merge.get());
    }

}

class TestFirstAggregateFunction<IN> extends AbstractFirstAggregateFunction<IN> {
}
