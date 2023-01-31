package com.yanggu.metric_calculate.core.unit.object;


import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ReplacedObjectUnit单元测试类
 *
 */
public class ReplacedObjectUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = ReplacedObjectUnit.class.getAnnotation(MergeType.class);
        assertEquals("REPLACEDOBJECT", annotation.value());
        assertFalse(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = ReplacedObjectUnit.class.getAnnotation(Objective.class);
        assertFalse(objective.useCompareField());
        assertTrue(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = new ReplacedObjectUnit<>();
        assertNull(replacedObjectUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = new ReplacedObjectUnit<>(key);
        assertEquals(key, replacedObjectUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = new ReplacedObjectUnit<>(key);

        ReplacedObjectUnit<Key<Integer>> fastClone = replacedObjectUnit.fastClone();

        assertNotSame(replacedObjectUnit, fastClone);
        assertEquals(replacedObjectUnit, fastClone);
        assertEquals(replacedObjectUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = new ReplacedObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        replacedObjectUnit.value(key);

        assertEquals(key, replacedObjectUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = new ReplacedObjectUnit<>();
        assertNull(replacedObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>value有数据时, 应该返回对应的数据
     */
    @Test
    public void testValue2() {
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = new ReplacedObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        replacedObjectUnit.value(key);

        assertEquals(key, replacedObjectUnit.value());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        ReplacedObjectUnit<Key<Integer>> replacedObjectUnit = (new ReplacedObjectUnit<>(key));

        replacedObjectUnit.merge(null);
        assertEquals(key, replacedObjectUnit.getValue());

        replacedObjectUnit.setValue(null);
        replacedObjectUnit.merge(new ReplacedObjectUnit<>(new Key<>(-1)));
        assertEquals(new Key<>(-1), replacedObjectUnit.getValue());

        replacedObjectUnit.setValue(key);
        replacedObjectUnit.merge(new ReplacedObjectUnit<>(new Key<>(-1)));
        assertEquals(new Key<>(-1), replacedObjectUnit.getValue());

        replacedObjectUnit.merge(new ReplacedObjectUnit<>(new Key<>(2)));
        assertEquals(new Key<>(2), replacedObjectUnit.getValue());
    }

}
