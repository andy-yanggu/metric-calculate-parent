package com.yanggu.metric_calculate.core.aggregate_function.collection;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 去重类的抽象函数基类单元测试类
 */
class AbstractDistinctAggregateFunctionTest {

    /**
     * 测试创建累加器是否正常
     */
    @Test
    void testCreateAccumulator() {
        var distinctAggregateFunction = new TestDistinctAggregateFunction<Integer>();
        var accumulator = distinctAggregateFunction.createAccumulator();
        //确认累加器不为null
        assertNotNull(accumulator);
        //确定累加器为空
        assertTrue(accumulator.isEmpty());
        //确认累加器是HashSet类型
        assertTrue(accumulator instanceof HashSet);
    }

    /**
     * 测试添加元素是否正常
     */
    @Test
    void testAdd() {
        var distinctAggregateFunction = new TestDistinctAggregateFunction<Integer>();
        var accumulator = distinctAggregateFunction.createAccumulator();
        distinctAggregateFunction.add(1, accumulator);
        //添加重复的元素
        distinctAggregateFunction.add(1, accumulator);
        //确认元素只被添加了一次
        assertTrue(accumulator.contains(1));
        //确认累加器中只包含一个元素
        assertEquals(1, accumulator.size());
    }

    /**
     * 测试获取结果是否正常
     */
    @Test
    void testGetResult() {
        var distinctAggregateFunction = new TestDistinctAggregateFunction<Integer>();
        var accumulator = distinctAggregateFunction.createAccumulator();
        accumulator = distinctAggregateFunction.add(1, accumulator);
        var result = distinctAggregateFunction.getResult(accumulator);
        //确认结果列表中包含已添加的元素
        assertTrue(result.contains(1));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
        //确认结果列表是ArrayList类型
        assertTrue(result instanceof ArrayList);
    }

    /**
     * 测试合并累加器是否正常
     */
    @Test
    void testMerge() {
        var distinctAggregateFunction = new TestDistinctAggregateFunction<Integer>();
        var thisAccumulator = distinctAggregateFunction.createAccumulator();
        distinctAggregateFunction.add(1, thisAccumulator);
        var thatAccumulator = distinctAggregateFunction.createAccumulator();
        distinctAggregateFunction.add(2, thatAccumulator);
        //向第二个累加器添加重复元素
        distinctAggregateFunction.add(1, thatAccumulator);
        distinctAggregateFunction.merge(thisAccumulator, thatAccumulator);
        //确认累加器中包含第一个累加器中的元素
        assertTrue(thisAccumulator.contains(1));
        //确认累加器中包含第一个累加器中的元素
        assertTrue(thisAccumulator.contains(2));
        //确认累加器中只有两个元素
        assertEquals(2, thisAccumulator.size());
    }

}

class TestDistinctAggregateFunction<IN> extends AbstractDistinctAggregateFunction<IN, List<IN>> {

    @Override
    public List<IN> getResult(Set<IN> acc) {
        return new ArrayList<>(acc);
    }
    
}
