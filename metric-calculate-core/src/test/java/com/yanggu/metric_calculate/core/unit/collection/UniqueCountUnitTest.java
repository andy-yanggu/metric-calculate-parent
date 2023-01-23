package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.value.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * UniqueCountUnit的单元测试类
 */
public class UniqueCountUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //去重计数

        //验证空参构造
        UniqueCountUnit<Key<String>> uniqueCountUnit = new UniqueCountUnit<>();
        Key<String> key = new Key<>("test1");
        uniqueCountUnit.add(key);
        assertEquals(1, uniqueCountUnit.value());

        //验证有参构造
        uniqueCountUnit = new UniqueCountUnit<>(key);
        assertEquals(1, uniqueCountUnit.value());

        //验证merge方法
        uniqueCountUnit.merge(new UniqueCountUnit<>(key));
        assertEquals(1, uniqueCountUnit.value());

        uniqueCountUnit.merge(new UniqueCountUnit<>(new Key<>("test2")));
        assertEquals(2, uniqueCountUnit.value());
    }

}