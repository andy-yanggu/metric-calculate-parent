package com.yanggu.metric_calculate.core.unit.obj;


import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ReplacedUnit单元测试类
 *
 */
public class ReplacedUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test1() {

        //验证空参构造
        ReplacedUnit<Key<String>> replacedUnit = new ReplacedUnit<>();
        Key<String> key = new Key<>("test");
        replacedUnit.value(key);
        assertEquals(key, replacedUnit.value());

        //验证有参构造
        replacedUnit = new ReplacedUnit<>(key);
        assertEquals(key, replacedUnit.value());

        //验证merge方法
        key = new Key<>("test2");
        replacedUnit.merge(new ReplacedUnit<>(key));
        assertEquals(key, replacedUnit.value());
    }

}
