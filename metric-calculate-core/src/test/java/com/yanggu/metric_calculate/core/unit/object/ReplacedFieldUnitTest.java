package com.yanggu.metric_calculate.core.unit.object;


import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ReplacedFieldUnit单元测试类
 *
 */
public class ReplacedFieldUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = ReplacedFieldUnit.class.getAnnotation(MergeType.class);
        assertEquals("LASTFIELD", annotation.value());
        assertFalse(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = ReplacedFieldUnit.class.getAnnotation(Objective.class);
        assertFalse(objective.useCompareField());
        assertFalse(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = new ReplacedFieldUnit<>();
        assertNull(replacedFieldUnit.getReplacedObjectUnit().getValueData());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = new ReplacedFieldUnit<>(key);
        assertEquals(key, replacedFieldUnit.getReplacedObjectUnit().getValueData());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = new ReplacedFieldUnit<>(key);

        ReplacedFieldUnit<Key<Integer>> fastClone = replacedFieldUnit.fastClone();

        assertNotSame(replacedFieldUnit, fastClone);
        assertEquals(replacedFieldUnit, fastClone);
        assertEquals(replacedFieldUnit.getReplacedObjectUnit().getValueData(), fastClone.getReplacedObjectUnit().getValueData());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = new ReplacedFieldUnit<>();
        Key<Integer> key = new Key<>(1);
        replacedFieldUnit.value(key);

        assertEquals(key, replacedFieldUnit.getReplacedObjectUnit().getValueData());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = new ReplacedFieldUnit<>();
        assertNull(replacedFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>value有数据时, 应该返回对应的数据
     */
    @Test
    public void testValue2() {
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = new ReplacedFieldUnit<>();
        Key<Integer> key = new Key<>(1);
        replacedFieldUnit.value(key);

        assertEquals(key, replacedFieldUnit.value());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        ReplacedFieldUnit<Key<Integer>> replacedFieldUnit = (new ReplacedFieldUnit<>(key));

        replacedFieldUnit.merge(null);
        assertEquals(key, replacedFieldUnit.value());

        replacedFieldUnit.getReplacedObjectUnit().setValueData(null);
        replacedFieldUnit.merge(new ReplacedFieldUnit<>(new Key<>(-1)));
        assertEquals(new Key<>(-1), replacedFieldUnit.value());

        replacedFieldUnit.getReplacedObjectUnit().setValueData(key);
        replacedFieldUnit.merge(new ReplacedFieldUnit<>(new Key<>(-1)));
        assertEquals(new Key<>(-1), replacedFieldUnit.value());

        replacedFieldUnit.merge(new ReplacedFieldUnit<>(new Key<>(2)));
        assertEquals(new Key<>(2), replacedFieldUnit.value());
    }

}
