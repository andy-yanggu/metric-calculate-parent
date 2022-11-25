package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.value.KeyValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortedListUnitTest {

    @Test
    public void test01() {
        KeyValue value1 = new KeyValue(1, 1);
        KeyValue value2 = new KeyValue(2, 2);
        KeyValue value3 = new KeyValue(0, 0);
        SortedListUnit unit = new SortedListUnit<>();
        unit.add(value1);
        unit.add(value2);
        unit.add(value3);
        assertEquals(Arrays.asList(value2, value1, value3), unit.asList());
        assertEquals(Arrays.asList(value2, value1, value3), unit.value());
    }

}