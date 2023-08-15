package com.yanggu.metric_calculate.core.aggregate_function;

import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctKey;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 状态窗口单元测试类
 */
class StateWindowAggregateFunctionTest {

    private StateWindowAggregateFunction<Integer, Double, Double> sumStateWindow;

    @BeforeEach
    void init() {
        sumStateWindow = new StateWindowAggregateFunction<>();
        sumStateWindow.setAggregateFunction(new SumAggregateFunction<>());
    }

    @Test
    void createAccumulator() {
        MutableEntry<MultiFieldDistinctKey, Double> accumulator = sumStateWindow.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.getKey());
        assertEquals(0.0D, accumulator.getValue(), 0.0D);
    }

    @Test
    void add1() {
        MutableEntry<MultiFieldDistinctKey, Double> accumulator = sumStateWindow.createAccumulator();
        MultiFieldDistinctKey key = new MultiFieldDistinctKey(Collections.singletonList("1"));
        MutableEntry<MultiFieldDistinctKey, Integer> input = new MutableEntry<>(key, 100);
        MutableEntry<MultiFieldDistinctKey, Double> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getKey());
        assertEquals(100.0D, add.getValue(), 0.0D);
    }

    @Test
    void add2() {
        MutableEntry<MultiFieldDistinctKey, Double> accumulator = sumStateWindow.createAccumulator();
        MultiFieldDistinctKey key = new MultiFieldDistinctKey(Collections.singletonList("1"));
        MutableEntry<MultiFieldDistinctKey, Integer> input = new MutableEntry<>(key, 100);
        MutableEntry<MultiFieldDistinctKey, Double> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getKey());
        assertEquals(100.0D, add.getValue(), 0.0D);

        add = sumStateWindow.add(input, add);
        assertSame(add, accumulator);
        assertEquals(key, add.getKey());
        assertEquals(200.0D, add.getValue(), 0.0D);

        //当状态改变时, 累加器应该重新累加
        MultiFieldDistinctKey key2 = new MultiFieldDistinctKey(Collections.singletonList("2"));
        MutableEntry<MultiFieldDistinctKey, Integer> input2 = new MutableEntry<>(key2, 200);
        add = sumStateWindow.add(input2, add);
        assertSame(add, accumulator);
        assertEquals(key2, add.getKey());
        assertEquals(200.0D, add.getValue(), 0.0D);
    }

    @Test
    void getResult() {
        MutableEntry<MultiFieldDistinctKey, Double> accumulator = sumStateWindow.createAccumulator();
        MultiFieldDistinctKey key = new MultiFieldDistinctKey(Collections.singletonList("1"));
        MutableEntry<MultiFieldDistinctKey, Integer> input = new MutableEntry<>(key, 100);
        MutableEntry<MultiFieldDistinctKey, Double> add = sumStateWindow.add(input, accumulator);
        add = sumStateWindow.add(input, add);

        MutableEntry<MultiFieldDistinctKey, Double> result = sumStateWindow.getResult(add);
        System.out.println(result);
    }

}