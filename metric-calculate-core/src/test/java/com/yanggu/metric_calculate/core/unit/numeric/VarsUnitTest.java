package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeDecimal;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class VarsUnitTest {

    @Test
    public void testVarsDecimal() {
        new BigDecimal(10.123123d);
        VarsUnit varsUnit = new VarsUnit(CubeDecimal.of("10"));
        varsUnit.merge(new VarsUnit(CubeDecimal.of("11")));
        assertTrue(Math.abs(varsUnit.doubleValue() - 0.5) < 0.0001);
    }

}
