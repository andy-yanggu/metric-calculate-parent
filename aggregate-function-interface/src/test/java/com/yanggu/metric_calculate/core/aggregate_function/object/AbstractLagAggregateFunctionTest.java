package com.yanggu.metric_calculate.core.aggregate_function.object;


import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 当前行的第前N条对象聚合函数单元测试类
 */
class AbstractLagAggregateFunctionTest {

    private TestLagAggregateFunction<String> lagObjectAggregateFunction;

    @BeforeEach
    void init() {
        lagObjectAggregateFunction = new TestLagAggregateFunction<>();
    }

    @Test
    void testCreateAccumulator() {
        LinkedList<String> accumulator = lagObjectAggregateFunction.createAccumulator();
        assertNotNull(accumulator);
        assertTrue(accumulator.isEmpty());
    }

    @Test
    void testAdd() {
        LinkedList<String> accumulator = lagObjectAggregateFunction.createAccumulator();

        lagObjectAggregateFunction.add("test1", accumulator);
        assertEquals(1, accumulator.size());
        assertEquals("test1", accumulator.get(0));

        lagObjectAggregateFunction.add("test2", accumulator);
        assertEquals(2, accumulator.size());
        assertEquals("test1", accumulator.get(0));
        assertEquals("test2", accumulator.get(1));

        lagObjectAggregateFunction.setOffset(2);
        lagObjectAggregateFunction.add("test3", accumulator);
        assertEquals(3, accumulator.size());
        assertEquals("test1", accumulator.get(0));
        assertEquals("test2", accumulator.get(1));
        assertEquals("test3", accumulator.get(2));

        lagObjectAggregateFunction.add("test4", accumulator);
        assertEquals(3, accumulator.size());
        assertEquals("test2", accumulator.get(0));
        assertEquals("test3", accumulator.get(1));
        assertEquals("test4", accumulator.get(2));
    }

    @Test
    void testGetResult() {
        lagObjectAggregateFunction.setDefaultValue("defaultValue");
        LinkedList<String> accumulator = lagObjectAggregateFunction.createAccumulator();

        lagObjectAggregateFunction.add("test1", accumulator);
        String result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("defaultValue", result);

        lagObjectAggregateFunction.add("test2", accumulator);
        result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("test1", result);

        lagObjectAggregateFunction.setOffset(2);
        accumulator = lagObjectAggregateFunction.createAccumulator();

        lagObjectAggregateFunction.add("test3", accumulator);
        result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("defaultValue", result);

        lagObjectAggregateFunction.add("test4", accumulator);
        result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("defaultValue", result);

        lagObjectAggregateFunction.add("test5", accumulator);
        result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("test3", result);

        lagObjectAggregateFunction.add("test6", accumulator);
        result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("test4", result);

        //重新设置offset
        lagObjectAggregateFunction.setOffset(1);
        result = lagObjectAggregateFunction.getResult(accumulator);
        assertEquals("test5", result);
    }

    @Test
    void testMerge() {
        lagObjectAggregateFunction.setOffset(4);

        LinkedList<String> accumulator1 = lagObjectAggregateFunction.createAccumulator();
        accumulator1.add("test1");
        accumulator1.add("test2");
        accumulator1.add("test3");
        accumulator1.add("test4");

        LinkedList<String> accumulator2 = lagObjectAggregateFunction.createAccumulator();
        accumulator2.add("test5");
        accumulator2.add("test6");

        LinkedList<String> merge = lagObjectAggregateFunction.merge(accumulator1, accumulator2);
        assertEquals(ListUtil.ofLinked("test2", "test3", "test4", "test5", "test6"), merge);
    }

}

class TestLagAggregateFunction<IN> extends AbstractLagAggregateFunction<IN> {
}
