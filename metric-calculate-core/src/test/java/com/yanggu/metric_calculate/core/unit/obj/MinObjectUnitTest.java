package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinObjectUnitTest {

    @Test
    public void test01() {
        KeyValue value1 = new KeyValue(1, 1);
        KeyValue value2 = new KeyValue(2, 2);
        KeyValue value3 = new KeyValue(2, 2);
        KeyValue value4 = new KeyValue(0, 0);
        MinObjectUnit unit = new MinObjectUnit<>(value1).value(value1);
        assertEquals(value1.value(), unit.value());
        unit.merge(new MinObjectUnit().value(value2));
        assertEquals(value1.value(), unit.value());
        unit.merge(new MinObjectUnit().value(value3));
        assertEquals(value1.value(), unit.value());
        unit.merge(new MinObjectUnit().value(value4));
        assertEquals(value4.value(), unit.value());
    }

}