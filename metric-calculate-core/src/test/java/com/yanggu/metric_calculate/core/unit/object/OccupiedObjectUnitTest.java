package com.yanggu.metric_calculate.core.unit.object;


import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * OccupiedObjectUnit的单元测试, 表示占位
 */
public class OccupiedObjectUnitTest {

    /**
     * 验证MergeType注解中的数据
     */
    @Test
    public void testMergeType() {
        MergeType annotation = OccupiedObjectUnit.class.getAnnotation(MergeType.class);
        assertEquals("OCCUPIEDOBJECT", annotation.value());
        assertFalse(annotation.useParam());
    }

    /**
     * 验证Objective注解中的数据
     */
    @Test
    public void testObjective() {
        Objective objective = OccupiedObjectUnit.class.getAnnotation(Objective.class);
        assertFalse(objective.useCompareField());
        assertTrue(objective.retainObject());
    }

    /**
     * 验证空参构造
     */
    @Test
    public void testConstructor1() {
        //验证空参构造
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = new OccupiedObjectUnit<>();
        assertNull(occupiedObjectUnit.getValue());
    }

    /**
     * 验证有参构造
     */
    @Test
    public void testConstructor2() {
        //验证有参构造
        Key<Integer> key = new Key<>(1);
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = new OccupiedObjectUnit<>(key);
        assertEquals(key, occupiedObjectUnit.getValue());
    }

    /**
     * 测试fastClone方法
     */
    @Test
    public void testFastClone() {
        Key<Integer> key = new Key<>(1);
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = new OccupiedObjectUnit<>(key);

        OccupiedObjectUnit<Key<Integer>> fastClone = occupiedObjectUnit.fastClone();

        assertNotSame(occupiedObjectUnit, fastClone);
        assertEquals(occupiedObjectUnit, fastClone);
        assertEquals(occupiedObjectUnit.getValue(), fastClone.getValue());
    }

    /**
     * 测试value(T object)方法
     */
    @Test
    public void testValueObject() {
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = new OccupiedObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        occupiedObjectUnit.value(key);

        assertEquals(key, occupiedObjectUnit.getValue());
    }

    /**
     * 测试value()方法
     * <p>当value没有值的时候应该返回null</p>
     */
    @Test
    public void testValue1() {
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = new OccupiedObjectUnit<>();
        assertNull(occupiedObjectUnit.value());
    }

    /**
     * 测试value()方法
     * <p>value有数据时, 应该返回对应的数据
     */
    @Test
    public void testValue2() {
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = new OccupiedObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        occupiedObjectUnit.value(key);

        assertEquals(key, occupiedObjectUnit.value());
    }

    /**
     * 测试merge方法
     */
    @Test
    public void testMerge() {
        //验证merge方法
        Key<Integer> key = new Key<>(0);
        OccupiedObjectUnit<Key<Integer>> occupiedObjectUnit = (new OccupiedObjectUnit<>(key));

        occupiedObjectUnit.merge(null);
        assertEquals(key, occupiedObjectUnit.getValue());

        occupiedObjectUnit.setValue(null);
        occupiedObjectUnit.merge(new OccupiedObjectUnit<>(new Key<>(-1)));
        assertEquals(new Key<>(-1), occupiedObjectUnit.getValue());

        occupiedObjectUnit.setValue(key);
        occupiedObjectUnit.merge(new OccupiedObjectUnit<>(new Key<>(-1)));
        assertEquals(key, occupiedObjectUnit.getValue());

        occupiedObjectUnit.merge(new OccupiedObjectUnit<>(new Key<>(2)));
        assertEquals(key, occupiedObjectUnit.getValue());
    }



}
