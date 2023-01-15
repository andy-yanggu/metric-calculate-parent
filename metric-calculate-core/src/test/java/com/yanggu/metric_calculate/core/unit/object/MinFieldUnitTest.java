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

/**
 * MinFieldUnit单元测试类
 */
public class MinFieldUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = MinFieldUnit.class.getAnnotation(MergeType.class);
        assertEquals("MINFIELD", annotation.value());
        assertTrue(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = MinFieldUnit.class.getAnnotation(Objective.class);
        assertTrue(objective.useCompareField());
        assertFalse(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        MinFieldUnit<Key<Integer>> minFieldUnit = new MinFieldUnit<>();
        assertTrue(minFieldUnit.getOnlyShowValue());
        assertNull(minFieldUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        MinFieldUnit<Key<Integer>> minFieldUnit = new MinFieldUnit<>(key);
        assertTrue(minFieldUnit.getOnlyShowValue());
        assertEquals(key, minFieldUnit.getValue());
    }

    /**
     * 验证param构造
     */
    @Test
    public void testConstructor3() {
        //验证param构造1
        Map<String, Object> param = new HashMap<>();
        MinFieldUnit<Key<Integer>> minFieldUnit = new MinFieldUnit<>(param);
        assertTrue(minFieldUnit.getOnlyShowValue());
        assertNull(minFieldUnit.getValue());


        //验证param构造2
        param.put(MinFieldUnit.Fields.onlyShowValue, null);
        minFieldUnit = new MinFieldUnit<>(param);
        assertTrue(minFieldUnit.getOnlyShowValue());
        assertNull(minFieldUnit.getValue());

        //验证param构造3
        param.put(MinFieldUnit.Fields.onlyShowValue, "test");
        minFieldUnit = new MinFieldUnit<>(param);
        assertTrue(minFieldUnit.getOnlyShowValue());
        assertNull(minFieldUnit.getValue());

        //验证param构造4
        param.put(MinFieldUnit.Fields.onlyShowValue, true);
        minFieldUnit = new MinFieldUnit<>(param);
        assertTrue(minFieldUnit.getOnlyShowValue());
        assertNull(minFieldUnit.getValue());

        //验证param构造5
        param.put(MinFieldUnit.Fields.onlyShowValue, false);
        minFieldUnit = new MinFieldUnit<>(param);
        assertFalse(minFieldUnit.getOnlyShowValue());
        assertNull(minFieldUnit.getValue());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        MinFieldUnit<Key<Integer>> minFieldUnit = (new MinFieldUnit<>(key));

        minFieldUnit.merge(null);
        assertEquals(key, minFieldUnit.getValue());

        key = new Key<>(-1);
        minFieldUnit.merge(new MinFieldUnit<>(key));
        assertEquals(key, minFieldUnit.getValue());

        minFieldUnit.merge(new MinFieldUnit<>(new Key<>(2)));
        assertEquals(key, minFieldUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        MinFieldUnit<Key<Integer>> minFieldUnit = new MinFieldUnit<>(key);

        MinFieldUnit<Key<Integer>> fastClone = minFieldUnit.fastClone();

        assertEquals(minFieldUnit, fastClone);
        assertEquals(minFieldUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        MinFieldUnit<Key<Integer>> minFieldUnit = new MinFieldUnit<>();
        Key<Integer> key = new Key<>(1);
        minFieldUnit.value(key);

        assertEquals(key, minFieldUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        MinFieldUnit<Key<Integer>> minFieldUnit = new MinFieldUnit<>();
        assertNull(minFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为false</p>
     * <p>应该执行this.value instanceof Value逻辑</p>
     */
    @Test
    public void testValue2() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(1, "张三");
        MinFieldUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> minFieldUnit = new MinFieldUnit<>(keyValue);
        minFieldUnit.setOnlyShowValue(false);
        assertEquals(keyValue.value(), minFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>应该返回原来的KeyValue的value数据</p>
     */
    @Test
    public void testValue3() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(1, "张三");
        MinFieldUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> minFieldUnit = new MinFieldUnit<>(keyValue);
        assertEquals(keyValue.getValue().value(), minFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value是KeyValue类型, onlyShowValue为true</p>
     * <p>但是KeyValue的Value没有赋值, 应该返回null</p>
     */
    @Test
    public void testValue4() {
        KeyValue<Key<Integer>, Cloneable2Wrapper<String>> keyValue = new KeyValue<>(1, null);
        MinFieldUnit<KeyValue<Key<Integer>, Cloneable2Wrapper<String>>> minFieldUnit = new MinFieldUnit<>(keyValue);
        assertNull(minFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>当value既不是Value类型也不是KeyValue类型, 应该返回原始数据</p>
     */
    @Test
    public void testValue5() {
        Key<String> key = new Key<>("张三");
        MinFieldUnit<Key<String>> minFieldUnit = new MinFieldUnit<>(key);
        assertEquals(key, minFieldUnit.value());
    }


}