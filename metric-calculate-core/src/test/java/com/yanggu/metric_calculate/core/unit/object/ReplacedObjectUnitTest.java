package com.yanggu.metric_calculate.core.unit.object;


import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * ReplacedUnit单元测试类
 *
 */
public class ReplacedObjectUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test1() {

        //验证空参构造
        ReplacedObjectUnit<Key<String>> replacedObjectUnit = new ReplacedObjectUnit<>();
        Key<String> key = new Key<>("test");
        replacedObjectUnit.value(key);
        assertEquals(key, replacedObjectUnit.value());

        //验证有参构造
        replacedObjectUnit = new ReplacedObjectUnit<>(key);
        assertEquals(key, replacedObjectUnit.value());

        //验证merge方法
        key = new Key<>("test2");
        replacedObjectUnit.merge(new ReplacedObjectUnit<>(key));
        assertEquals(key, replacedObjectUnit.value());
    }

}
