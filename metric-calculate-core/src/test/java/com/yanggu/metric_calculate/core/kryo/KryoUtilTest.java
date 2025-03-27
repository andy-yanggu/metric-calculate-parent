package com.yanggu.metric_calculate.core.kryo;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.FunctionFactory;
import com.yanggu.metric_calculate.core.kryo.pool.InputPool;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.kryo.pool.OutputPool;
import com.yanggu.metric_calculate.core.util.TestJarUtil;
import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * KryoUtil测试类
 */
class KryoUtilTest {

    private KryoUtil kryoUtil;

    @BeforeEach
    void init() {
        KryoPool kryoPool = new KryoPool(100, null);
        InputPool inputPool = new InputPool(100);
        OutputPool outputPool = new OutputPool(100);
        this.kryoUtil = new KryoUtil(kryoPool, inputPool, outputPool);
    }

    @Test
    void testMutablePair1() {
        MutablePair<String, Integer> pair = new MutablePair<>("zhangsan", 1);
        byte[] serialize = kryoUtil.serialize(pair);
        Object deserialize = kryoUtil.deserialize(serialize);
        assertEquals(pair, deserialize);
    }

    /**
     * 测试Jar包中的ACC能够序列化和反序列化成功
     */
    @Test
    @Disabled("不一定能成功，测试jar包不一定存在")
    void test2() throws Exception {
        List<String> jarPathList = TestJarUtil.testJarPath();
        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(jarPathList);
        aggregateFunctionFactory.init();
        AggregateFunction<Object, Object, Object> testAcc = aggregateFunctionFactory.getAggregateFunction("TEST_ACC");
        Map<String, Object> params = new HashMap<>();
        FunctionFactory.setParam(testAcc, params);
        Object accumulator = testAcc.createAccumulator();

        KryoPool kryoPool = new KryoPool(100, AggregateFunctionFactory.ACC_CLASS_LOADER);
        InputPool inputPool = new InputPool(100);
        OutputPool outputPool = new OutputPool(100);
        KryoUtil tempKryoUtil = new KryoUtil(kryoPool, inputPool, outputPool);
        byte[] serialize = tempKryoUtil.serialize(accumulator);
        Object deserialize = tempKryoUtil.deserialize(serialize);
        //由于使用不同类加载器加载, 这里一定是false
        //assertEquals(accumulator, deserialize);
        assertEquals(accumulator.toString(), deserialize.toString());
    }

}
