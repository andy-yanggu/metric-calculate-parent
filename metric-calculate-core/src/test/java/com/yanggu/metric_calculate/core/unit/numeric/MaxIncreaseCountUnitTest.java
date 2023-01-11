package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeLong;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxIncreaseCountUnitTest {

    @Test
    public void testLong01() {
        MaxIncreaseCountUnit unit = new MaxIncreaseCountUnit(CubeLong.of(10));
        unit.merge(new MaxIncreaseCountUnit(CubeLong.of(11)));
        assertEquals(1, unit.intValue());
        unit.merge(new MaxIncreaseCountUnit(CubeLong.of(12)));
        assertEquals(2, unit.intValue());
        unit.merge(new MaxIncreaseCountUnit(CubeLong.of(10)));
        assertEquals(2, unit.intValue());
    }

}