package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * MinObjectUnit单元测试类
 */
public class MinObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //验证空参构造
        MinObjectUnit<Key<Integer>> minObjectUnit = new MinObjectUnit<>();
        Key<Integer> key = new Key<>(1);
        minObjectUnit.value(key);
        assertEquals(key, minObjectUnit.value());

        //验证有参构造
        minObjectUnit = new MinObjectUnit<>(key);
        assertEquals(key, minObjectUnit.value());

        //验证merge方法
        key = new Key<>(0);
        minObjectUnit.merge(new MinObjectUnit<>(key));
        assertEquals(key, minObjectUnit.value());

        key = new Key<>(-1);
        minObjectUnit.merge(new MinObjectUnit<>(key));
        assertEquals(key, minObjectUnit.value());

        minObjectUnit.merge(new MinObjectUnit<>(new Key<>(2)));
        assertEquals(key, minObjectUnit.value());
    }

}