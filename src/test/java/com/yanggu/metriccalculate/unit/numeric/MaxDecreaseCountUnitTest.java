package com.yanggu.metriccalculate.unit.numeric;

import com.yanggu.metriccalculate.number.CubeLong;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaxDecreaseCountUnitTest {

    @Test
    public void testLong01() {
        MaxDecreaseCountUnit unit = new MaxDecreaseCountUnit(CubeLong.of(10));
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(11)));
        Assertions.assertEquals(0, unit.intValue());
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(9)));
        Assertions.assertEquals(1, unit.intValue());
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(8)));
        Assertions.assertEquals(2, unit.intValue());
        unit.merge(new MaxDecreaseCountUnit(CubeLong.of(8)));
        Assertions.assertEquals(2, unit.intValue());
    }

}