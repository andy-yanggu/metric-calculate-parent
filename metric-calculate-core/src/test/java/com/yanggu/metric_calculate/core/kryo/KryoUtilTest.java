package com.yanggu.metric_calculate.core.kryo;

import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * KryoUtil测试类
 */
class KryoUtilTest {

    @Test
    void testMutablePair1() {
        MutablePair<String, Integer> pair = new MutablePair<>("zhangsan", 1);
        byte[] serialize = KryoUtil.serialize(pair);
        Object deserialize = KryoUtil.deserialize(serialize);
        assertEquals(pair, deserialize);
    }

}
