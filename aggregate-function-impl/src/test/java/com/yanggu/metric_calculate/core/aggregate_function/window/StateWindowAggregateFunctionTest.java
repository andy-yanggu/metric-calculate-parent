package com.yanggu.metric_calculate.core.aggregate_function.window;

import com.yanggu.metric_calculate.core.aggregate_function.numeric.SumAggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 状态窗口单元测试类
 */
class StateWindowAggregateFunctionTest {

    private StateWindowAggregateFunction<String, Integer, BigDecimal, BigDecimal> sumStateWindow;

    @BeforeEach
    void init() {
        sumStateWindow = new StateWindowAggregateFunction<>();
        sumStateWindow.setAggregateFunction(new SumAggregateFunction<>());
    }

    @Test
    void createAccumulator() {
        MutablePair<String, BigDecimal> accumulator = sumStateWindow.createAccumulator();
        assertNotNull(accumulator);
        assertNull(accumulator.getLeft());
        assertEquals(0.0D, accumulator.getRight().doubleValue(), 0.0D);
    }

    @Test
    void add1() {
        MutablePair<String, BigDecimal> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutablePair<String, Integer> input = new MutablePair<>(key, 100);
        MutablePair<String, BigDecimal> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getLeft());
        assertEquals(100.0D, add.getRight().doubleValue(), 0.0D);
    }

    @Test
    void add2() {
        MutablePair<String, BigDecimal> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutablePair<String, Integer> input = new MutablePair<>(key, 100);
        MutablePair<String, BigDecimal> add = sumStateWindow.add(input, accumulator);
        assertSame(add, accumulator);
        assertEquals(key, add.getLeft());
        assertEquals(100.0D, add.getRight().doubleValue(), 0.0D);

        add = sumStateWindow.add(input, add);
        assertSame(add, accumulator);
        assertEquals(key, add.getLeft());
        assertEquals(200.0D, add.getRight().doubleValue(), 0.0D);

        //当状态改变时, 累加器应该重新累加
        String key2 = "2";
        MutablePair<String, Integer> input2 = new MutablePair<>(key2, 200);
        add = sumStateWindow.add(input2, add);
        assertSame(add, accumulator);
        assertEquals(key2, add.getLeft());
        assertEquals(200.0D, add.getRight().doubleValue(), 0.0D);
    }

    @Test
    void getResult() {
        MutablePair<String, BigDecimal> accumulator = sumStateWindow.createAccumulator();
        String key = "1";
        MutablePair<String, Integer> input = new MutablePair<>(key, 100);
        MutablePair<String, BigDecimal> add = sumStateWindow.add(input, accumulator);
        add = sumStateWindow.add(input, add);

        MutablePair<String, BigDecimal> result = sumStateWindow.getResult(add);
        System.out.println(result);
    }

}