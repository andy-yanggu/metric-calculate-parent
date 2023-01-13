package com.yanggu.metric_calculate.core.unit.collect;

import com.yanggu.metric_calculate.core.unit.collection.ListUnit;
import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * ListUnit单员测试类
 */
public class ListUnitTest {

    /**
     * 验证空参构造、有参构造和merge方法
     */
    @Test
    public void test01() {
        //验证空参构造
        ListUnit<Key<String>> listUnit = new ListUnit<>();
        Key<String> key = new Key<>("test");
        listUnit.add(key);
        assertEquals(Collections.singletonList(key), listUnit.value());

        //验证有参构造
        listUnit = new ListUnit<>(key);
        assertEquals(Collections.singletonList(key), listUnit.value());

        //验证merge方法
        listUnit.merge(new ListUnit<>(new Key<>("test2")));
        assertEquals(Arrays.asList(key, new Key<>("test2")), listUnit.value());
        listUnit.merge(new ListUnit<>(new Key<>("test3")));
        assertEquals(Arrays.asList(key, new Key<>("test2"), new Key<>("test3")), listUnit.value());
    }

}