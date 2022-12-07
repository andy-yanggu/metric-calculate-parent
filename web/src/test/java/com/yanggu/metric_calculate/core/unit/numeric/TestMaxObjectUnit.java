package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeDouble;
import com.yanggu.metric_calculate.core.number.CubeLong;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class TestMaxObjectUnit {

    @Test
    public void testSumLong() {
        MaxUnit maxUnit = new MaxUnit<>(CubeLong.of(10L));
        maxUnit.merge(new MaxUnit<>(CubeLong.of(10L)));
        assertEquals(10L, maxUnit.value());
        maxUnit.merge(new MaxUnit<>(CubeLong.of(5L), 3));
        assertEquals(10L, maxUnit.value());
        maxUnit.merge(new MaxUnit<>(CubeLong.of(50L), 1));
        assertEquals(50L, maxUnit.value());
    }

    @Test
    public void testAvgDecimal() {
        MaxUnit maxUnit = new MaxUnit<>(CubeDecimal.of(new BigDecimal("10")));
        maxUnit.merge(new MaxUnit<>(CubeDecimal.of(new BigDecimal("10"))));
        assertEquals(new BigDecimal("10"), maxUnit.value());
        maxUnit.merge(new MaxUnit<>(CubeDecimal.of(new BigDecimal("5")), 3));
        assertEquals(new BigDecimal("10"), maxUnit.value());
        maxUnit.merge(new MaxUnit<>(CubeDecimal.of("50"), 1));
        assertEquals(new BigDecimal("50"), maxUnit.value());
    }

    @Test
    public void testAvgDouble() {
        MaxUnit maxUnit = new MaxUnit<>(CubeDouble.of(10.0));
        maxUnit.merge(new MaxUnit<>(CubeDouble.of(10.0)));
        assertTrue(Math.abs(10 - maxUnit.value().doubleValue()) < 0.0001);
        maxUnit.merge(new MaxUnit<>(CubeDouble.of(5.0), 3));
        assertTrue(Math.abs(10 - maxUnit.value().doubleValue()) < 0.0001);
        maxUnit.merge(new MaxUnit<>(CubeDouble.of(50.0), 1));
        assertTrue(Math.abs(50 - maxUnit.value().doubleValue()) < 0.0001);
    }

}
