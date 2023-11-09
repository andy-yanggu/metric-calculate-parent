package com.yanggu.metric_calculate.core.kryo;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.FunctionFactory;
import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * KryoUtil测试类
 */
class KryoUtilTest {

    @Test
    void testMutablePair1() {
        MutablePair<String, Integer> pair = new MutablePair<>("zhangsan", 1);
        //byte[] serialize = KryoUtil.serialize(pair);
        //Object deserialize = KryoUtil.deserialize(serialize);
        //assertEquals(pair, deserialize);
    }

    @Test
    void test2() throws Exception {
        String jarPath = "D:\\project\\self\\metric-calculate-parent\\metric-calculate-test\\target\\metric-calculate-test-1.0.0-SNAPSHOT.jar";
        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(List.of(jarPath));
        aggregateFunctionFactory.init();
        AggregateFunction<Object, Object, Object> testListobject = aggregateFunctionFactory.getAggregateFunction("TEST_LISTOBJECT");
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 20);
        FunctionFactory.setParam(testListobject, params);
        //byte[] serialize = KryoUtil.serialize(testListobject);
        //Object deserialize = KryoUtil.deserialize(serialize);
        //System.out.println(deserialize);
    }

}
