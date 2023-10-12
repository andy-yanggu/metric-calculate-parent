package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 替换聚合函数抽象类单元测试类
 */
class AbstractLastAggregateFunctionTest {

    @Test
    void createAccumulator() {
        LastAggregateFunction<String> lastObjectAggregateFunction = new LastAggregateFunction<>();
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.get());
    }

    @Test
    void add() {
        LastAggregateFunction<String> lastObjectAggregateFunction = new LastAggregateFunction<>();
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator);
        assertEquals("test1", accumulator.get());

        lastObjectAggregateFunction.add("test2", accumulator);
        assertEquals("test2", accumulator.get());
    }

    @Test
    void getResult() {
        LastAggregateFunction<String> lastObjectAggregateFunction = new LastAggregateFunction<>();
        MutableObj<String> accumulator = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator);
        String result = lastObjectAggregateFunction.getResult(accumulator);

        assertEquals("test1", result);
    }

    @Test
    void merge() {
        LastAggregateFunction<String> lastObjectAggregateFunction = new LastAggregateFunction<>();
        MutableObj<String> accumulator1 = lastObjectAggregateFunction.createAccumulator();
        MutableObj<String> accumulator2 = lastObjectAggregateFunction.createAccumulator();

        lastObjectAggregateFunction.add("test1", accumulator1);
        lastObjectAggregateFunction.add("test2", accumulator2);

        MutableObj<String> merge = lastObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals("test2", merge.get());
    }

}

class LastAggregateFunction<T> extends AbstractLastAggregateFunction<T> {
}