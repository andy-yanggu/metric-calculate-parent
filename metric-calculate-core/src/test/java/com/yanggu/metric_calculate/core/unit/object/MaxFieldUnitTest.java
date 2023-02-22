package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * MaxFieldUnit单元测试类
 */
public class MaxFieldUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = MaxFieldUnit.class.getAnnotation(MergeType.class);
        assertEquals("MAXFIELD", annotation.value());
        assertTrue(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = MaxFieldUnit.class.getAnnotation(Objective.class);
        assertTrue(objective.useCompareField());
        assertFalse(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>();
        assertTrue(maxFieldUnit.getOnlyShowValue());
        assertNull(maxFieldUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>(key);
        assertTrue(maxFieldUnit.getOnlyShowValue());
        assertEquals(key, maxFieldUnit.getValue());
    }

    /**
     * 验证param构造
     */
    @Test
    public void testConstructor3() {
        //验证param构造1
        Map<String, Object> param = new HashMap<>();
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>(param);
        assertTrue(maxFieldUnit.getOnlyShowValue());
        assertNull(maxFieldUnit.getValue());

        //验证param构造2
        param.put("onlyShowValue", null);
        maxFieldUnit = new MaxFieldUnit<>(param);
        assertTrue(maxFieldUnit.getOnlyShowValue());
        assertNull(maxFieldUnit.getValue());

        //验证param构造3
        param.put("onlyShowValue", "test");
        maxFieldUnit = new MaxFieldUnit<>(param);
        assertTrue(maxFieldUnit.getOnlyShowValue());
        assertNull(maxFieldUnit.getValue());

        //验证param构造4
        param.put("onlyShowValue", true);
        maxFieldUnit = new MaxFieldUnit<>(param);
        assertTrue(maxFieldUnit.getOnlyShowValue());
        assertNull(maxFieldUnit.getValue());

        //验证param构造5
        param.put("onlyShowValue", false);
        maxFieldUnit = new MaxFieldUnit<>(param);
        assertFalse(maxFieldUnit.getOnlyShowValue());
        assertNull(maxFieldUnit.getValue());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>(key);

        maxFieldUnit.merge(null);
        assertEquals(key, maxFieldUnit.getValue());

        key = new Key<>(-1);
        maxFieldUnit.merge(new MaxFieldUnit<>(key));
        assertEquals(new Key<>(0), maxFieldUnit.getValue());

        maxFieldUnit.merge(new MaxFieldUnit<>(new Key<>(2)));
        assertEquals(new Key<>(2), maxFieldUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>(key);

        MaxFieldUnit<Key<Integer>> fastClone = maxFieldUnit.fastClone();

        assertNotSame(maxFieldUnit, fastClone);
        assertEquals(maxFieldUnit, fastClone);
        assertEquals(maxFieldUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>();
        Key<Integer> key = new Key<>(1);
        maxFieldUnit.value(key);

        assertEquals(key, maxFieldUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        MaxFieldUnit<Key<Integer>> maxFieldUnit = new MaxFieldUnit<>();
        assertNull(maxFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为false</p>
     * <p>应该执行this.value instanceof Value逻辑</p>
     */
    @Test
    public void testValue2() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(new Key<>(1), Cloneable2Wrapper.wrap("张三"));
        MaxFieldUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> maxFieldUnit = new MaxFieldUnit<>(keyValue);
        maxFieldUnit.setOnlyShowValue(false);
        assertEquals(keyValue.value(), maxFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>应该返回原来的KeyValue的value数据</p>
     */
    @Test
    public void testValue3() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(new Key<>(1), Cloneable2Wrapper.wrap("张三"));
        MaxFieldUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> maxFieldUnit = new MaxFieldUnit<>(keyValue);
        assertEquals(keyValue.getValue().value(), maxFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>但是KeyValue的Value没有赋值, 应该返回null</p>
     */
    @Test
    public void testValue4() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(new Key<>(1), null);
        MaxFieldUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> maxFieldUnit = new MaxFieldUnit<>(keyValue);
        assertNull(maxFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value既不是Value类型也不是KeyValue类型, 应该返回原始数据</p>
     */
    @Test
    public void testValue5() {
        Key<String> key = new Key<>("张三");
        MaxFieldUnit<Key<String>> maxFieldUnit = new MaxFieldUnit<>(key);
        assertEquals(key, maxFieldUnit.value());
    }

}
