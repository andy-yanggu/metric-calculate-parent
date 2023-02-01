package com.yanggu.metric_calculate.core.unit.collection;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.object.MaxFieldUnit;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * ListFieldUnit单元测试类
 */
public class ListFieldUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = ListFieldUnit.class.getAnnotation(MergeType.class);
        assertEquals("LISTFIELD", annotation.value());
        assertTrue(annotation.useParam());
    }

    /**
     * 验证Collective注解中的数据
     */
    @Test
    public void testCollective() {
        Collective objective = ListFieldUnit.class.getAnnotation(Collective.class);
        assertFalse(objective.useCompareField());
        assertFalse(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        ListFieldUnit<Key<Integer>> listFieldUnit = new ListFieldUnit<>();
        //验证空参构造
        assertNotNull(listFieldUnit.getValues());
        assertTrue(listFieldUnit.getValues().isEmpty());
        assertEquals(new Integer(0), listFieldUnit.getLimit());

    }
    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        Key<Integer> key = new Key<>(1);
        ListFieldUnit<Key<Integer>> listFieldUnit = new ListFieldUnit<>(key);
        assertSame(key, listFieldUnit.getValues().get(0));
        assertEquals(key, listFieldUnit.getValues().get(0));
    }

    /**
     * 验证param构造
     */
    @Test
    public void testConstructor3() {
        //验证param构造1
        Map<String, Object> param = new HashMap<>();
        ListFieldUnit<Key<Integer>> listFieldUnit = new ListFieldUnit<>(param);
        assertTrue(CollUtil.isEmpty(listFieldUnit.getValues()));
        assertEquals(new Integer(0), listFieldUnit.getLimit());

        param.put("limit", null);
        listFieldUnit = new ListFieldUnit<>(param);
        assertTrue(CollUtil.isEmpty(listFieldUnit.getValues()));
        assertEquals(new Integer(0), listFieldUnit.getLimit());

        param.put("limit", "test");
        listFieldUnit = new ListFieldUnit<>(param);
        assertTrue(CollUtil.isEmpty(listFieldUnit.getValues()));
        assertEquals(new Integer(0), listFieldUnit.getLimit());

        param.put("limit", 1);
        listFieldUnit = new ListFieldUnit<>(param);
        assertTrue(CollUtil.isEmpty(listFieldUnit.getValues()));
        assertEquals(new Integer(1), listFieldUnit.getLimit());
    }

    /**
     * 测试add方法
     * 当limit为0的时候, 不限制长度
     */
    @Test
    public void testAdd1() {
        ListFieldUnit<Key<Integer>> listFieldUnit = new ListFieldUnit<>();

        Key<Integer> key = new Key<>(1);
        listFieldUnit.add(key);

        assertEquals(Collections.singletonList(key), listFieldUnit.getValues());

        listFieldUnit.add(key);
        assertEquals(Arrays.asList(key, key), listFieldUnit.getValues());
    }

    /**
     * 测试add方法
     * 当limit不为0的时候, 限制长度
     */
    @Test
    public void testAdd2() {
        Map<String, Object> param = new HashMap<>();
        param.put("limit", 3);

        ListFieldUnit<Key<Integer>> listFieldUnit = new ListFieldUnit<>(param);

        Key<Integer> key = new Key<>(1);
        listFieldUnit.add(key);

        assertEquals(Collections.singletonList(key), listFieldUnit.getValues());

        listFieldUnit.add(key);
        assertEquals(Arrays.asList(key, key), listFieldUnit.getValues());

        listFieldUnit.add(key);
        assertEquals(Arrays.asList(key, key, key), listFieldUnit.getValues());

        //当加入第4个数据, 会超过长度, 进行移除, 只存储3个
        listFieldUnit.add(key);
        assertEquals(Arrays.asList(key, key, key), listFieldUnit.getValues());
    }

}
