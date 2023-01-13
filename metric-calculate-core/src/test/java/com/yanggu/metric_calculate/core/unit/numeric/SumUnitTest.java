package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeDouble;
import com.yanggu.metric_calculate.core.number.CubeLong;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SumUnitTest {

    @Test
    public void testSumLong() {
        SumUnit sumUnit = new SumUnit<>(CubeLong.of(10L));
        sumUnit.merge(new SumUnit<>(CubeLong.of(10L)));
        assertEquals(20L, sumUnit.value());
        sumUnit.merge(new SumUnit<>(CubeLong.of(5L), 3));
        assertEquals(25L, sumUnit.value());
        sumUnit.merge(new SumUnit<>(CubeLong.of(5L), 1));
        assertEquals(30L, sumUnit.value());
    }

    @Test
    public void testAvgDecimal() {
        SumUnit sumUnit = new SumUnit<>(CubeDecimal.of(new BigDecimal("10")));
        sumUnit.merge(new SumUnit<>(CubeDecimal.of(new BigDecimal("10"))));
        assertEquals(new BigDecimal("20"), sumUnit.value());
        sumUnit.merge(new SumUnit<>(CubeDecimal.of(new BigDecimal("5")), 3));
        assertEquals(new BigDecimal("25"), sumUnit.value());
        sumUnit.merge(new SumUnit<>(CubeDecimal.of("5"), 1));
        assertEquals(new BigDecimal("30"), sumUnit.value());
    }

    @Test
    public void testAvgDouble() {
        SumUnit sumUnit = new SumUnit<>(CubeDouble.of(10.0));
        sumUnit.merge(new SumUnit<>(CubeDouble.of(10.0)));
        assertTrue(Math.abs(20 - sumUnit.value().doubleValue()) < 0.0001);
        sumUnit.merge(new SumUnit<>(CubeDouble.of(5.0), 3));
        assertTrue(Math.abs(25 - sumUnit.value().doubleValue()) < 0.0001);
        sumUnit.merge(new SumUnit<>(CubeDouble.of(5.0), 1));
        assertTrue(Math.abs(30 - sumUnit.value().doubleValue()) < 0.0001);
    }

}
