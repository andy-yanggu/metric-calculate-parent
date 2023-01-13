package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeDouble;
import com.yanggu.metric_calculate.core.number.CubeLong;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AvgMixedUnitTest {

    @Test
    public void testAvgLong() {
        AvgMixedUnit avgUnit = new AvgMixedUnit<>(CubeLong.of(1L), CubeLong.of(10L));
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(1L), CubeLong.of(10L)));
        assertEquals(10L, avgUnit.value());
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(3L), CubeLong.of(5L)));
        assertEquals(5L, avgUnit.value());
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(0L), CubeLong.of(5L)));
        assertEquals(6L, avgUnit.value());
    }

    @Test
    public void testAvgDecimal() {
        AvgMixedUnit avgUnit = new AvgMixedUnit<>(CubeLong.of(1L), CubeDecimal.of(new BigDecimal("10")));
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(1L), CubeDecimal.of(new BigDecimal("10"))));
        assertEquals(new BigDecimal("10").setScale(16), avgUnit.value());
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(3L), CubeDecimal.of(new BigDecimal("5"))));
        assertEquals(new BigDecimal("5").setScale(16), avgUnit.value());
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(0L), CubeDecimal.of("5")));
        assertEquals(new BigDecimal("6").setScale(16), avgUnit.value());
    }

    @Test
    public void testAvgDouble() {
        AvgMixedUnit avgUnit = new AvgMixedUnit<>(CubeLong.of(1L), CubeDouble.of(10.0));
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(1L), CubeDouble.of(10.0)));
        assertTrue(Math.abs(10.0 - avgUnit.value().doubleValue()) < 0.0001);
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(3L), CubeDouble.of(5.0)));
        assertTrue(Math.abs(5.0 - avgUnit.value().doubleValue()) < 0.0001);
        avgUnit.merge(new AvgMixedUnit<>(CubeLong.of(0L), CubeDouble.of(5.0)));
        assertTrue(Math.abs(6.0 - avgUnit.value().doubleValue()) < 0.0001);
    }

}
