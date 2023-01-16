package com.yanggu.metric_calculate.core.unit.object;


import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * OccupiedFieldUnit的单元测试, 表示占位
 */
public class OccupiedFieldUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = OccupiedFieldUnit.class.getAnnotation(MergeType.class);
        assertEquals("OCCUPIEDFIELD", annotation.value());
        assertFalse(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = OccupiedFieldUnit.class.getAnnotation(Objective.class);
        assertFalse(objective.useCompareField());
        assertFalse(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = new OccupiedFieldUnit<>();
        assertNull(occupiedFieldUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = new OccupiedFieldUnit<>(key);
        assertEquals(key, occupiedFieldUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = new OccupiedFieldUnit<>(key);

        OccupiedFieldUnit<Key<Integer>> fastClone = occupiedFieldUnit.fastClone();

        assertNotSame(occupiedFieldUnit, fastClone);
        assertEquals(occupiedFieldUnit, fastClone);
        assertEquals(occupiedFieldUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = new OccupiedFieldUnit<>();
        Key<Integer> key = new Key<>(1);
        occupiedFieldUnit.value(key);

        assertEquals(key, occupiedFieldUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = new OccupiedFieldUnit<>();
        assertNull(occupiedFieldUnit.value());
    }

    /**
     * 测试value()方法
     * <p>value有数据时, 应该返回对应的数据
     */
    @Test
    public void testValue2() {
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = new OccupiedFieldUnit<>();
        Key<Integer> key = new Key<>(1);
        occupiedFieldUnit.value(key);

        assertEquals(key, occupiedFieldUnit.value());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        OccupiedFieldUnit<Key<Integer>> occupiedFieldUnit = (new OccupiedFieldUnit<>(key));

        occupiedFieldUnit.merge(null);
        assertEquals(key, occupiedFieldUnit.getValue());

        occupiedFieldUnit.setValue(null);
        occupiedFieldUnit.merge(new OccupiedFieldUnit<>(new Key<>(-1)));
        assertEquals(new Key<>(-1), occupiedFieldUnit.getValue());

        occupiedFieldUnit.setValue(key);
        occupiedFieldUnit.merge(new OccupiedFieldUnit<>(new Key<>(-1)));
        assertEquals(key, occupiedFieldUnit.getValue());

        occupiedFieldUnit.merge(new OccupiedFieldUnit<>(new Key<>(2)));
        assertEquals(key, occupiedFieldUnit.getValue());
    }

}
