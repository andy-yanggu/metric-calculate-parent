package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeDouble;
import com.yanggu.metric_calculate.core.number.CubeLong;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMinObjectUnit {

    @Test
    public void testSumLong() {
        MinUnit minUnit = new MinUnit<>(CubeLong.of(7L));
        minUnit.merge(new MinUnit<>(CubeLong.of(10L)));
        assertEquals(7L, minUnit.value());
        minUnit.merge(new MinUnit<>(CubeLong.of(5L), 3));
        assertEquals(5L, minUnit.value());
        minUnit.merge(new MinUnit<>(CubeLong.of(1L), 1));
        assertEquals(1L, minUnit.value());
    }

    @Test
    public void testAvgDecimal() {
        MinUnit minUnit = new MinUnit<>(CubeDecimal.of(new BigDecimal("7")));
        minUnit.merge(new MinUnit<>(CubeDecimal.of(new BigDecimal("10"))));
        assertEquals(new BigDecimal("7"), minUnit.value());
        minUnit.merge(new MinUnit<>(CubeDecimal.of(new BigDecimal("5")), 3));
        assertEquals(new BigDecimal("5"), minUnit.value());
        minUnit.merge(new MinUnit<>(CubeDecimal.of("1"), 1));
        assertEquals(new BigDecimal("1"), minUnit.value());
    }

    @Test
    public void testAvgDouble() {
        MinUnit minUnit = new MinUnit<>(CubeDouble.of(7.0));
        minUnit.merge(new MinUnit<>(CubeDouble.of(10.0)));
        assertTrue(Math.abs(7 - minUnit.value().doubleValue()) < 0.0001);
        minUnit.merge(new MinUnit<>(CubeDouble.of(5.0), 3));
        assertTrue(Math.abs(5 - minUnit.value().doubleValue()) < 0.0001);
        minUnit.merge(new MinUnit<>(CubeDouble.of(1.0), 1));
        assertTrue(Math.abs(1 - minUnit.value().doubleValue()) < 0.0001);
    }

}
