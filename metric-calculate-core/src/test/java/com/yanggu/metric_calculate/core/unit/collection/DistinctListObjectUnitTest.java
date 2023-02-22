package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * UniqueListUnit单元测试类
 */
public class DistinctListObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //去重列表

        //验证空参构造
        DistinctListObjectUnit<Key<String>> distinctListObjectUnit = new DistinctListObjectUnit<>();
        Key<String> key = new Key<>("test1");
        distinctListObjectUnit.add(key);
        assertEquals(new HashSet<>(Collections.singletonList(key)), distinctListObjectUnit.value());

        //验证有参构造
        distinctListObjectUnit = new DistinctListObjectUnit<>(key);
        assertEquals(new HashSet<>(Collections.singletonList(key)), distinctListObjectUnit.value());

        //验证merge方法
        distinctListObjectUnit.merge(new DistinctListObjectUnit<>(key));
        assertEquals(new HashSet<>(Collections.singletonList(key)), distinctListObjectUnit.value());

        Key<String> key2 = new Key<>("test2");
        distinctListObjectUnit.merge(new DistinctListObjectUnit<>(key2));
        assertEquals(new HashSet<>(Arrays.asList(key, key2)), distinctListObjectUnit.value());

    }

}