package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.value.KeyValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaxUnitTest {

    @Test
    public void test01() {
        KeyValue value1 = new KeyValue(1, 1);
        KeyValue value2 = new KeyValue(2, 2);
        KeyValue value3 = new KeyValue(2, 2);
        KeyValue value4 = new KeyValue(3, 3);
        MaxUnit unit = new MaxUnit<>(value1).value(value1);
        assertEquals(value1.value(), unit.value());
        unit.merge(new MaxUnit().value(value2));
        assertEquals(value2.value(), unit.value());
        unit.merge(new MaxUnit().value(value3));
        assertEquals(value2.value(), unit.value());
        unit.merge(new MaxUnit().value(value4));
        assertEquals(value4.value(), unit.value());
    }

}