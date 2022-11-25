package com.yanggu.metriccalculate.unit.numeric;

import com.yanggu.metriccalculate.number.CubeDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestVarp {

    @Test
    public void testVarpDecimal() {
        VarpUnit varpUnit = new VarpUnit<>(CubeDecimal.of("10"));
        varpUnit.merge(new VarpUnit<>(CubeDecimal.of("11")));
        assertTrue(Math.abs(varpUnit.doubleValue() - 0.25) < 0.0001);
    }


}
