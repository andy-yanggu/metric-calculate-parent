package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MaxObjectUnit单元测试类
 */
public class MaxObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //验证空参构造
        MaxObjectUnit<Key<Integer>> maxObjectUnit = new MaxObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        maxObjectUnit.value(key);
        assertEquals(key, maxObjectUnit.value());

        //验证有参构造
        maxObjectUnit = new MaxObjectUnit<>(key);
        assertEquals(key, maxObjectUnit.value());

        //验证merge方法
        key = new Key<>(2);
        maxObjectUnit.merge(new MaxObjectUnit<>(key));
        assertEquals(key, maxObjectUnit.value());

        key = new Key<>(3);
        maxObjectUnit.merge(new MaxObjectUnit<>(key));
        assertEquals(key, maxObjectUnit.value());

        maxObjectUnit.merge(new MaxObjectUnit<>(new Key<>(2)));
        assertEquals(key, maxObjectUnit.value());
    }

}