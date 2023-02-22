package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * MaxObjectUnit单元测试类
 */
public class MaxObjectUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = MaxObjectUnit.class.getAnnotation(MergeType.class);
        assertEquals("MAXOBJECT", annotation.value());
        assertTrue(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = MaxObjectUnit.class.getAnnotation(Objective.class);
        assertTrue(objective.useCompareField());
        assertTrue(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>();
        assertTrue(maxObjectUnit.getOnlyShowValue());
        assertNull(maxObjectUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>(key);
        assertTrue(maxObjectUnit.getOnlyShowValue());
        assertEquals(key, maxObjectUnit.getValue());
    }

    /**
     * 验证param构造
     */
    @Test
    public void testConstructor3() {
        //验证param构造1
        Map<String, Object> param = new HashMap<>();
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>(param);
        assertTrue(maxObjectUnit.getOnlyShowValue());
        assertNull(maxObjectUnit.getValue());

        //验证param构造2
        param.put("onlyShowValue", null);
        maxObjectUnit = new MaxObjectUnit<>(param);
        assertTrue(maxObjectUnit.getOnlyShowValue());
        assertNull(maxObjectUnit.getValue());

        //验证param构造3
        param.put("onlyShowValue", "test");
        maxObjectUnit = new MaxObjectUnit<>(param);
        assertTrue(maxObjectUnit.getOnlyShowValue());
        assertNull(maxObjectUnit.getValue());

        //验证param构造4
        param.put("onlyShowValue", true);
        maxObjectUnit = new MaxObjectUnit<>(param);
        assertTrue(maxObjectUnit.getOnlyShowValue());
        assertNull(maxObjectUnit.getValue());

        //验证param构造5
        param.put("onlyShowValue", false);
        maxObjectUnit = new MaxObjectUnit<>(param);
        assertFalse(maxObjectUnit.getOnlyShowValue());
        assertNull(maxObjectUnit.getValue());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        MaxObjectUnit<Key<Integer>> maxObjectUnit = (new MaxObjectUnit<>(key));

        maxObjectUnit.merge(null);
        assertEquals(key, maxObjectUnit.getValue());

        key = new Key<>(-1);
        maxObjectUnit.merge(new MaxObjectUnit<>(key));
        assertEquals(new Key<>(0), maxObjectUnit.getValue());

        maxObjectUnit.merge(new MaxObjectUnit<>(new Key<>(2)));
        assertEquals(new Key<>(2), maxObjectUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>(key);

        MaxObjectUnit<Key<Integer>> fastClone = maxObjectUnit.fastClone();

        assertEquals(maxObjectUnit, fastClone);
        assertEquals(maxObjectUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        maxObjectUnit.value(key);

        assertEquals(key, maxObjectUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>();
        assertNull(maxObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为false</p>
     * <p>应该执行this.value instanceof Value逻辑</p>
     */
    @Test
    public void testValue2() {
        KeyValue<Key<Integer>, CloneWrapper<String>> keyValue = new KeyValue<>(new Key<>(1), CloneWrapper.wrap("张三"));
        MaxObjectUnit<KeyValue<Key<Integer>, CloneWrapper<String>>> maxObjectUnit = new MaxObjectUnit<>(keyValue);
        maxObjectUnit.setOnlyShowValue(false);
        assertEquals(keyValue.value(), maxObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>应该返回原来的KeyValue的value数据</p>
     */
    @Test
    public void testValue3() {
        KeyValue<Key<Integer>, CloneWrapper<String>> keyValue = new KeyValue<>(new Key<>(1), CloneWrapper.wrap("张三"));
        MaxObjectUnit<KeyValue<Key<Integer>, CloneWrapper<String>>> maxObjectUnit = new MaxObjectUnit<>(keyValue);
        assertEquals(keyValue.getValue().value(), maxObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>但是KeyValue的Value没有赋值, 应该返回null</p>
     */
    @Test
    public void testValue4() {
        KeyValue<Key<Integer>, CloneWrapper<String>> keyValue = new KeyValue<>(new Key<>(1), null);
        MaxObjectUnit<KeyValue<Key<Integer>, CloneWrapper<String>>> maxObjectUnit = new MaxObjectUnit<>(keyValue);
        assertNull(maxObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value既不是Value类型也不是KeyValue类型, 应该返回原始数据</p>
     */
    @Test
    public void testValue5() {
        Key<String> key = new Key<>("张三");
        MaxObjectUnit<Key<String>> maxObjectUnit = new MaxObjectUnit<>(key);
        assertEquals(key, maxObjectUnit.value());
    }

}