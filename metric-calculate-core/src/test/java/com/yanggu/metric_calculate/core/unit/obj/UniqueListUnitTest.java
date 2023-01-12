package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.unit.collection.UniqueListUnit;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class UniqueListUnitTest {

    @Test
    public void test01() {
        KeyValue value1 = new KeyValue(1, 1);
        KeyValue value2 = new KeyValue(2, 2);
        KeyValue value3 = new KeyValue(0, 0);
        KeyValue value4 = new KeyValue(0, 0);
        UniqueListUnit unit = new UniqueListUnit<>();
        unit.add(value1);
        unit.add(value2);
        unit.add(value3);
        unit.add(value4);
        assertEquals(new HashSet<>(Arrays.asList(value1, value2, value3)), unit.asCollection());
        assertEquals(new HashSet<>(Arrays.asList(value1, value2, value3)), unit.value());
    }

}