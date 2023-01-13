package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeDecimal;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class VarpTest {

    @Test
    public void testVarpDecimal() {
        VarpUnit varpUnit = new VarpUnit<>(CubeDecimal.of("10"));
        varpUnit.merge(new VarpUnit<>(CubeDecimal.of("11")));
        assertTrue(Math.abs(varpUnit.doubleValue() - 0.25) < 0.0001);
    }


}
