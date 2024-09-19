package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 替换聚合函数抽象类单元测试类
 */
class AbstractLastAggregateFunctionTest {

    private TestLastAggregateFunction<String> lastObjectAggregateFunction;

    @BeforeEach
    void init() {
        lastObjectAggregateFunction = new TestLastAggregateFunction<>();
    }

    @Test
    void testCreateAccumulator() {
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void testAdd() {
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        lastObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test2", accumulator.get());
    }

    @Test
    void testGetResult() {
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator);
        String result = lastObjectAggregateFunction.getResult(accumulator);
        assertEquals("test1", result);
    }

    @Test
    void testMerge() {
        MutableObj<String> accumulator1 = lastObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator1);
        lastObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = lastObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test2", merge.get());
    }

}

class TestLastAggregateFunction<IN> extends AbstractLastAggregateFunction<IN> {
}
