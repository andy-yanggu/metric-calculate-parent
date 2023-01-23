package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * SortedList单元测试类
 */
public class SortedListObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //验证空参构造
        SortedListObjectUnit<Key<Integer>> sortedListObjectUnit = new SortedListObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        sortedListObjectUnit.add(key);
        assertEquals(Collections.singletonList(key), sortedListObjectUnit.value());

        //验证有参构造
        sortedListObjectUnit = new SortedListObjectUnit<>(key);
        assertEquals(Collections.singletonList(key), sortedListObjectUnit.value());

        //验证merge方法
        Key<Integer> key2 = new Key<>(2);
        sortedListObjectUnit.merge(new SortedListObjectUnit<>(key2));
        assertEquals(Arrays.asList(key2, key), sortedListObjectUnit.value());
    }

}