package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * ListUnit单员测试类
 */
public class ListObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //验证空参构造
        ListObjectUnit<Key<String>> listObjectUnit = new ListObjectUnit<>();
        Key<String> key = new Key<>("test");
        listObjectUnit.add(key);
        assertEquals(Collections.singletonList(key), listObjectUnit.value());

        //验证有参构造
        listObjectUnit = new ListObjectUnit<>(key);
        assertEquals(Collections.singletonList(key), listObjectUnit.value());

        //验证merge方法
        listObjectUnit.merge(new ListObjectUnit<>(new Key<>("test2")));
        assertEquals(Arrays.asList(key, new Key<>("test2")), listObjectUnit.value());
        listObjectUnit.merge(new ListObjectUnit<>(new Key<>("test3")));
        assertEquals(Arrays.asList(key, new Key<>("test2"), new Key<>("test3")), listObjectUnit.value());
    }

}