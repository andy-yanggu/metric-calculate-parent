package com.yanggu.metric_calculate.core.unit.obj;


import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * OccupiedUnit的单元测试, 表示占位
 */
public class OccupiedObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test1() {
        Key<String> key = new Key<>("test");

        //验证空参构造
        OccupiedObjectUnit<Key<String>> occupiedObjectUnit = new OccupiedObjectUnit<>();
        occupiedObjectUnit.value(key);
        assertEquals(key, occupiedObjectUnit.value());

        //验证有参构造
        occupiedObjectUnit = new OccupiedObjectUnit<>(key);
        assertEquals(key, occupiedObjectUnit.value());

        //验证merge方法
        occupiedObjectUnit.merge(new OccupiedObjectUnit<>(new Key<>("李四")));
        assertEquals(key, occupiedObjectUnit.value());
    }

}
