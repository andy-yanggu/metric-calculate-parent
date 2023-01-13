package com.yanggu.metric_calculate.core.unit.collect;

import com.yanggu.metric_calculate.core.unit.collection.UniqueListUnit;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * UniqueListUnit单元测试类
 */
public class UniqueListUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //去重列表

        //验证空参构造
        UniqueListUnit<Key<String>> uniqueListUnit = new UniqueListUnit<>();
        Key<String> key = new Key<>("test1");
        uniqueListUnit.add(key);
        assertEquals(new HashSet<>(Collections.singletonList(key)), uniqueListUnit.value());

        //验证有参构造
        uniqueListUnit = new UniqueListUnit<>(key);
        assertEquals(new HashSet<>(Collections.singletonList(key)), uniqueListUnit.value());

        //验证merge方法
        uniqueListUnit.merge(new UniqueListUnit<>(key));
        assertEquals(new HashSet<>(Collections.singletonList(key)), uniqueListUnit.value());

        Key<String> key2 = new Key<>("test2");
        uniqueListUnit.merge(new UniqueListUnit<>(key2));
        assertEquals(new HashSet<>(Arrays.asList(key, key2)), uniqueListUnit.value());

    }

}