package com.yanggu.metric_calculate.core.unit.collect;

import com.yanggu.metric_calculate.core.unit.collection.SortedListUnit;
import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * SortedList单元测试类
 */
public class SortedListUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //验证空参构造
        SortedListUnit<Key<Integer>> sortedListUnit = new SortedListUnit<>();
        Key<Integer> key = new Key<>(1);
        sortedListUnit.add(key);
        assertEquals(Collections.singletonList(key), sortedListUnit.value());

        //验证有参构造
        sortedListUnit = new SortedListUnit<>(key);
        assertEquals(Collections.singletonList(key), sortedListUnit.value());

        //验证merge方法
        Key<Integer> key2 = new Key<>(2);
        sortedListUnit.merge(new SortedListUnit<>(key2));
        assertEquals(Arrays.asList(key2, key), sortedListUnit.value());
    }

}