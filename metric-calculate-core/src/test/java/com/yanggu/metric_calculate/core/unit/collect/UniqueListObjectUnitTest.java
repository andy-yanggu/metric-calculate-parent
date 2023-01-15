package com.yanggu.metric_calculate.core.unit.collect;

import com.yanggu.metric_calculate.core.unit.collection.UniqueListObjectUnit;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * UniqueListUnit单元测试类
 */
public class UniqueListObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //去重列表

        //验证空参构造
        UniqueListObjectUnit<Key<String>> uniqueListObjectUnit = new UniqueListObjectUnit<>();
        Key<String> key = new Key<>("test1");
        uniqueListObjectUnit.add(key);
        assertEquals(new HashSet<>(Collections.singletonList(key)), uniqueListObjectUnit.value());

        //验证有参构造
        uniqueListObjectUnit = new UniqueListObjectUnit<>(key);
        assertEquals(new HashSet<>(Collections.singletonList(key)), uniqueListObjectUnit.value());

        //验证merge方法
        uniqueListObjectUnit.merge(new UniqueListObjectUnit<>(key));
        assertEquals(new HashSet<>(Collections.singletonList(key)), uniqueListObjectUnit.value());

        Key<String> key2 = new Key<>("test2");
        uniqueListObjectUnit.merge(new UniqueListObjectUnit<>(key2));
        assertEquals(new HashSet<>(Arrays.asList(key, key2)), uniqueListObjectUnit.value());

    }

}