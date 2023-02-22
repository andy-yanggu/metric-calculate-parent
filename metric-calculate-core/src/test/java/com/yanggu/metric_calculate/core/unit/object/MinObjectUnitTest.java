package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.unit.object.MinObjectUnit.Fields;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * MinObjectUnit单元测试类
 */
public class MinObjectUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = MinObjectUnit.class.getAnnotation(MergeType.class);
        assertEquals("MINOBJECT", annotation.value());
        assertTrue(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = MinObjectUnit.class.getAnnotation(Objective.class);
        assertTrue(objective.useCompareField());
        assertTrue(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>();
        assertTrue(minObjectUnit.getOnlyShowValue());
        assertNull(minObjectUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>(key);
        assertTrue(minObjectUnit.getOnlyShowValue());
        assertEquals(key, minObjectUnit.getValue());
    }

    /**
     * 验证param构造
     */
    @Test
    public void testConstructor3() {
        //验证param构造1
        Map<String, Object> param = new HashMap<>();
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>(param);
        assertTrue(minObjectUnit.getOnlyShowValue());
        assertNull(minObjectUnit.getValue());

        //验证param构造2
        param.put(Fields.onlyShowValue, null);
        minObjectUnit = new MinObjectUnit<>(param);
        assertTrue(minObjectUnit.getOnlyShowValue());
        assertNull(minObjectUnit.getValue());

        //验证param构造3
        param.put(Fields.onlyShowValue, "test");
        minObjectUnit = new MinObjectUnit<>(param);
        assertTrue(minObjectUnit.getOnlyShowValue());
        assertNull(minObjectUnit.getValue());

        //验证param构造4
        param.put(Fields.onlyShowValue, true);
        minObjectUnit = new MinObjectUnit<>(param);
        assertTrue(minObjectUnit.getOnlyShowValue());
        assertNull(minObjectUnit.getValue());

        //验证param构造5
        param.put(Fields.onlyShowValue, false);
        minObjectUnit = new MinObjectUnit<>(param);
        assertFalse(minObjectUnit.getOnlyShowValue());
        assertNull(minObjectUnit.getValue());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        MinObjectUnit<Key<Integer>> minObjectUnit = (new MinObjectUnit<>(key));

        minObjectUnit.merge(null);
        assertEquals(key, minObjectUnit.getValue());

        key = new Key<>(-1);
        minObjectUnit.merge(new MinObjectUnit<>(key));
        assertEquals(key, minObjectUnit.getValue());

        minObjectUnit.merge(new MinObjectUnit<>(new Key<>(2)));
        assertEquals(key, minObjectUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>(key);

        MinObjectUnit<Key<Integer>> fastClone = minObjectUnit.fastClone();

        assertEquals(minObjectUnit, fastClone);
        assertEquals(minObjectUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        minObjectUnit.value(key);

        assertEquals(key, minObjectUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>();
        assertNull(minObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为false</p>
     * <p>应该执行this.value instanceof Value逻辑</p>
     */
    @Test
    public void testValue2() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(new Key<>(1), Cloneable2Wrapper.wrap("张三"));
        MinObjectUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> minObjectUnit = new MinObjectUnit<>(keyValue);
        minObjectUnit.setOnlyShowValue(false);
        assertEquals(keyValue.value(), minObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>应该返回原来的KeyValue的value数据</p>
     */
    @Test
    public void testValue3() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(new Key<>(1), Cloneable2Wrapper.wrap("张三"));
        MinObjectUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> minObjectUnit = new MinObjectUnit<>(keyValue);
        assertEquals(keyValue.getValue().value(), minObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>但是KeyValue的Value没有赋值, 应该返回null</p>
     */
    @Test
    public void testValue4() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(new Key<>(1), null);
        MinObjectUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> minObjectUnit = new MinObjectUnit<>(keyValue);
        assertNull(minObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value既不是Value类型也不是KeyValue类型, 应该返回原始数据</p>
     */
    @Test
    public void testValue5() {
        Key<String> key = new Key<>("张三");
        MinObjectUnit<Key<String>> minObjectUnit = new MinObjectUnit<>(key);
        assertEquals(key, minObjectUnit.value());
    }

}