package com.yanggu.metric_calculate.core.aggregate_function;

import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 状态窗口单元测试类
 */
class StateWindowAggregateFunctionTest {

    private StateWindowAggregateFunction<String, Integer, Double, Double> sumStateWindow;

    @BeforeEach
    void init() {
        sumStateWindow = new StateWindowAggregateFunction<>();
        sumStateWindow.setAggregateFunction(new SumAggregateFunction<>());
    }

    @Test
    void createAccumulator() {
        MutableEntry<String, Double> accumulator = sumStateWindow.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.getKey());
        assertEquals(0.0D, accumulator.getValue(), 0.0D);
    }

    @Test
    void add1() {
        MutableEntry<String, Double> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutableEntry<String, Integer> input = new MutableEntry<>(key, 100);
        MutableEntry<String, Double> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getKey());
        assertEquals(100.0D, add.getValue(), 0.0D);
    }

    @Test
    void add2() {
        MutableEntry<String, Double> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutableEntry<String, Integer> input = new MutableEntry<>(key, 100);
        MutableEntry<String, Double> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getKey());
        assertEquals(100.0D, add.getValue(), 0.0D);

        add = sumStateWindow.add(input, add);
        assertSame(add, accumulator);
        assertEquals(key, add.getKey());
        assertEquals(200.0D, add.getValue(), 0.0D);

        //当状态改变时, 累加器应该重新累加
        String key2 = "2";
        MutableEntry<String, Integer> input2 = new MutableEntry<>(key2, 200);
        add = sumStateWindow.add(input2, add);
        assertSame(add, accumulator);
        assertEquals(key2, add.getKey());
        assertEquals(200.0D, add.getValue(), 0.0D);
    }

    @Test
    void getResult() {
        MutableEntry<String, Double> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutableEntry<String, Integer> input = new MutableEntry<>(key, 100);
        MutableEntry<String, Double> add = sumStateWindow.add(input, accumulator);
        add = sumStateWindow.add(input, add);

        MutableEntry<String, Double> result = sumStateWindow.getResult(add);
        System.out.println(result);
    }

}