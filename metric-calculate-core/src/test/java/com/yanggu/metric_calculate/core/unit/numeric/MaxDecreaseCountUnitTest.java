package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeLong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxDecreaseCountUnitTest {

    @Test
    public void testLong01() {
        MaxDecreaseCountUnit unit = new MaxDecreaseCountUnit(CubeLong.of(10));
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(11)));
        assertEquals(0, unit.intValue());
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(9)));
        assertEquals(1, unit.intValue());
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(8)));
        assertEquals(2, unit.intValue());
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(8)));
        assertEquals(2, unit.intValue());
    }

}