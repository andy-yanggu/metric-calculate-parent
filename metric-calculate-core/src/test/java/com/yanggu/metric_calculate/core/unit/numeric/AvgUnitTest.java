package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeDouble;
import com.yanggu.metric_calculate.core.number.CubeLong;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;


/**
 * 平均值AvgUnit单元测试类
 */
public class AvgUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = AvgUnit.class.getAnnotation(MergeType.class);
        assertEquals("AVG", annotation.value());
        assertFalse(annotation.useParam());
    }

    /**
     * 验证Numerical注解存在
     */
    @Test
    public void testNumerical() {
        assertTrue(AvgUnit.class.isAnnotationPresent(Numerical.class));
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        AvgUnit<CubeLong> avgUnit = new AvgUnit<>();
        assertNull(avgUnit.getValue());
        assertEquals(CubeLong.of(0L), avgUnit.getCount());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证空参构造
        AvgUnit<CubeLong> avgUnit = new AvgUnit<>(CubeLong.of(1L));
        assertEquals(CubeLong.of(1L), avgUnit.getValue());
        assertEquals(CubeLong.of(1L), avgUnit.getCount());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor3() {
        //验证空参构造
        AvgUnit<CubeLong> avgUnit = new AvgUnit<>(CubeLong.of(1L), 2L);
        assertEquals(CubeLong.of(1L), avgUnit.getValue());
        assertEquals(CubeLong.of(2L), avgUnit.getCount());
    }

    @Test
    public void testAvgLong() {
        AvgUnit<CubeLong> avgUnit = new AvgUnit<>(CubeLong.of(10L));
        avgUnit.merge(new AvgUnit<>(CubeLong.of(10L)));
        assertEquals(10L, avgUnit.value());
        avgUnit.merge(new AvgUnit<>(CubeLong.of(5L), 3));
        assertEquals(7L, avgUnit.value());
        avgUnit.merge(new AvgUnit<>(CubeLong.of(5L), 1));
        assertEquals(6L, avgUnit.value());
    }

    @Test
    public void testAvgDecimal() {
        AvgUnit<CubeDecimal> avgUnit = new AvgUnit<>(CubeDecimal.of(new BigDecimal("10")));
        avgUnit.merge(new AvgUnit<>(CubeDecimal.of(new BigDecimal("10"))));
        assertEquals(new BigDecimal("10").setScale(16), avgUnit.value());
        avgUnit.merge(new AvgUnit<>(CubeDecimal.of(new BigDecimal("5")), 3));
        assertEquals(new BigDecimal("7").setScale(16), avgUnit.value());
        avgUnit.merge(new AvgUnit<>(CubeDecimal.of("5"), 1));
        assertEquals(new BigDecimal("6.6666666666666667"), avgUnit.value());
    }

    @Test
    public void testAvgDouble() {
        AvgUnit<CubeDouble> avgUnit = new AvgUnit<>(CubeDouble.of(10.0));
        avgUnit.merge(new AvgUnit<>(CubeDouble.of(10.0)));
        assertTrue(Math.abs(10.0 - avgUnit.value().doubleValue()) < 0.0001);
        avgUnit.merge(new AvgUnit<>(CubeDouble.of(5.0), 3));
        assertTrue(Math.abs(7.0 - avgUnit.value().doubleValue()) < 0.0001);
        avgUnit.merge(new AvgUnit<>(CubeDouble.of(5.0), 1));
        assertTrue(Math.abs(6.66667 - avgUnit.value().doubleValue()) < 0.0001);
    }

}
