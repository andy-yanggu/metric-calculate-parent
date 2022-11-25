package com.yanggu.metriccalculate.unit.numeric;

import com.yanggu.metriccalculate.number.CubeLong;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaxIncreaseCountUnitTest {

    @Test
    public void testLong01() {
        MaxIncreaseCountUnit unit = new MaxIncreaseCountUnit(CubeLong.of(10));
        unit.merge(new MaxIncreaseCountUnit(CubeLong.of(11)));
        Assertions.assertEquals(1, unit.intValue());
        unit.merge(new MaxIncreaseCountUnit(CubeLong.of(12)));
        Assertions.assertEquals(2, unit.intValue());
        unit.merge(new MaxIncreaseCountUnit(CubeLong.of(10)));
        Assertions.assertEquals(2, unit.intValue());
    }

}