package com.yanggu.metric_calculate.core.unit.collection;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;

/**
 * ListObjectUnit单元测试类
 */
public class ListObjectUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = ListObjectUnit.class.getAnnotation(MergeType.class);
        assertEquals("LISTOBJECT", annotation.value());
        assertTrue(annotation.useParam());
    }

    /**
     * 验证Collective注解中的数据
     */
    @Test
    public void testCollective() {
        Collective collective = ListObjectUnit.class.getAnnotation(Collective.class);
        assertFalse(collective.useDistinctField());
        assertFalse(collective.useSortedField());
        assertTrue(collective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>();
        //验证空参构造
        assertNotNull(listObjectUnit.getValues());
        assertTrue(listObjectUnit.getValues().isEmpty());
        assertEquals(new Integer(0), listObjectUnit.getLimit());

    }
    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        Key<Integer> key = new Key<>(1);
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>(key);
        assertSame(key, listObjectUnit.getValues().get(0));
        assertEquals(key, listObjectUnit.getValues().get(0));
    }

    /**
     * 验证param构造
     */
    @Test
    public void testConstructor3() {
        //验证param构造1
        Map<String, Object> param = new HashMap<>();
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>(param);
        assertTrue(CollUtil.isEmpty(listObjectUnit.getValues()));
        assertEquals(new Integer(0), listObjectUnit.getLimit());

        param.put("limit", null);
        listObjectUnit = new ListObjectUnit<>(param);
        assertTrue(CollUtil.isEmpty(listObjectUnit.getValues()));
        assertEquals(new Integer(0), listObjectUnit.getLimit());

        param.put("limit", "test");
        listObjectUnit = new ListObjectUnit<>(param);
        assertTrue(CollUtil.isEmpty(listObjectUnit.getValues()));
        assertEquals(new Integer(0), listObjectUnit.getLimit());

        param.put("limit", 1);
        listObjectUnit = new ListObjectUnit<>(param);
        assertTrue(CollUtil.isEmpty(listObjectUnit.getValues()));
        assertEquals(new Integer(1), listObjectUnit.getLimit());
    }

    /**
     * 测试add方法
     * 当limit为0的时候, 不限制长度
     */
    @Test
    public void testAdd1() {
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>();

        Key<Integer> key = new Key<>(1);
        listObjectUnit.add(key);

        assertEquals(Collections.singletonList(key), listObjectUnit.getValues());

        listObjectUnit.add(key);
        assertEquals(Arrays.asList(key, key), listObjectUnit.getValues());
    }

    /**
     * 测试add方法
     * 当limit不为0的时候, 限制长度
     */
    @Test
    public void testAdd2() {
        Map<String, Object> param = new HashMap<>();
        param.put("limit", 3);

        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>(param);

        Key<Integer> key = new Key<>(1);
        listObjectUnit.add(key);

        assertEquals(Collections.singletonList(key), listObjectUnit.getValues());

        listObjectUnit.add(key);
        assertEquals(Arrays.asList(key, key), listObjectUnit.getValues());

        listObjectUnit.add(key);
        assertEquals(Arrays.asList(key, key, key), listObjectUnit.getValues());

        //当加入第4个数据, 会超过长度, 进行移除, 只存储3个
        listObjectUnit.add(key);
        assertEquals(Arrays.asList(key, key, key), listObjectUnit.getValues());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        Map<String, Object> param = new HashMap<>();
        param.put("limit", 3);
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>(param);

        listObjectUnit.add(new Key<>(1));
        listObjectUnit.add(new Key<>(2));
        listObjectUnit.add(new Key<>(3));

        ListObjectUnit<Key<Integer>> listObjectUnit2 = new ListObjectUnit<>(param);
        listObjectUnit2.add(new Key<>(4));
        listObjectUnit2.add(new Key<>(5));

        listObjectUnit.merge(listObjectUnit2);
        assertEquals(Arrays.asList(new Key<>(1), new Key<>(2), new Key<>(3)), listObjectUnit.getValues());
    }

    /**
     * 测试value()方法
     */
    @Test
    public void testValue() {
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>();
        assertSame(listObjectUnit.getValues(), listObjectUnit.value());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        ListObjectUnit<Key<Integer>> listObjectUnit = new ListObjectUnit<>();
        listObjectUnit.add(new Key<>(1));

        ListObjectUnit<Key<Integer>> fastClone = listObjectUnit.fastClone();
        assertNotSame(listObjectUnit, fastClone);
        assertEquals(listObjectUnit, fastClone);
    }

}