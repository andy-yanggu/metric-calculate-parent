package com.yanggu.metric_calculate.core.unit.obj;


import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * OccupiedUnit的单元测试, 表示占位
 */
public class OccupiedUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test1() {
        Key<String> key = new Key<>("test");

        //验证空参构造
        OccupiedUnit<Key<String>> occupiedUnit = new OccupiedUnit<>();
        occupiedUnit.value(key);
        assertEquals(key, occupiedUnit.value());

        //验证有参构造
        occupiedUnit = new OccupiedUnit<>(key);
        assertEquals(key, occupiedUnit.value());

        //验证merge方法
        occupiedUnit.merge(new OccupiedUnit<>(new Key<>("李四")));
        assertEquals(key, occupiedUnit.value());
    }

}
