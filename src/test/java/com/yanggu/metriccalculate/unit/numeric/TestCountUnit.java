package com.yanggu.metriccalculate.unit.numeric;

import com.yanggu.metriccalculate.number.CubeLong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCountUnit {

    @Test
    public void testAvgLong() {
        CountUnit countUnit = new CountUnit(CubeLong.of(10L));
        countUnit.merge(new CountUnit(CubeLong.of(10L)));
        assertEquals(20L, countUnit.value());
        countUnit.merge(new CountUnit(CubeLong.of(5L), 3));
        assertEquals(25L, countUnit.value());
        countUnit.merge(new CountUnit(CubeLong.of(5L), 1));
        assertEquals(30L, countUnit.value());
    }

}
