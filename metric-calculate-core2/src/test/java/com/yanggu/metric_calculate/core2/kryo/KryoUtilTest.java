package com.yanggu.metric_calculate.core2.kryo;

import cn.hutool.core.lang.mutable.MutablePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * KryoUtil测试类
 */
public class KryoUtilTest {

    @Test
    public void testMutablePair1() {
        MutablePair<String, Integer> pair = new MutablePair<>("zhangsan", 1);
        byte[] serialize = KryoUtil.serialize(pair);
        Object deserialize = KryoUtil.deserialize(serialize);
        assertEquals(pair, deserialize);
    }

}
