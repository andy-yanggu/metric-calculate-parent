package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ListUnitTest {

    @Test
    public void test01() {
        KeyValue value1 = new KeyValue(1, 1);
        KeyValue value2 = new KeyValue(2, 2);
        ListUnit unit = new ListUnit<>();
        unit.add(value1);
        unit.add(value2);
        assertEquals(Arrays.asList(value1, value2), unit.asList());
        assertEquals(Arrays.asList(value1, value2), unit.value());
    }

}