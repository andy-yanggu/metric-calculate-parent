package com.yanggu.metric_calculate.core.aggregate_function;

import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        MutablePair<String, Double> accumulator = sumStateWindow.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.getLeft());
        assertEquals(0.0D, accumulator.getRight(), 0.0D);
    }

    @Test
    void add1() {
        MutablePair<String, Double> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutablePair<String, Integer> input = new MutablePair<>(key, 100);
        MutablePair<String, Double> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getLeft());
        assertEquals(100.0D, add.getRight(), 0.0D);
    }

    @Test
    void add2() {
        MutablePair<String, Double> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutablePair<String, Integer> input = new MutablePair<>(key, 100);
        MutablePair<String, Double> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getLeft());
        assertEquals(100.0D, add.getRight(), 0.0D);

        add = sumStateWindow.add(input, add);
        assertSame(add, accumulator);
        assertEquals(key, add.getLeft());
        assertEquals(200.0D, add.getRight(), 0.0D);

        //当状态改变时, 累加器应该重新累加
        String key2 = "2";
        MutablePair<String, Integer> input2 = new MutablePair<>(key2, 200);
        add = sumStateWindow.add(input2, add);
        assertSame(add, accumulator);
        assertEquals(key2, add.getLeft());
        assertEquals(200.0D, add.getRight(), 0.0D);
    }

    @Test
    void getResult() {
        MutablePair<String, Double> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutablePair<String, Integer> input = new MutablePair<>(key, 100);
        MutablePair<String, Double> add = sumStateWindow.add(input, accumulator);
        add = sumStateWindow.add(input, add);

        MutablePair<String, Double> result = sumStateWindow.getResult(add);
        System.out.println(result);
    }

}
