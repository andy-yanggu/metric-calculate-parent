package com.yanggu.metric_calculate.core.aggregate_function;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.*;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 聚合函数单元测试基类
 */
public class AggregateFunctionTestBase {

    /**
     * 验证AggregateFunctionAnnotation中的name数据
     *
     * @param clazz
     * @param name
     */
    public static void testAggregateFunctionName(Class<? extends AggregateFunction> clazz, String name) {
        AggregateFunctionAnnotation annotation = clazz.getAnnotation(AggregateFunctionAnnotation.class);
        assertNotNull(annotation);
        assertEquals(name, annotation.name());
    }

    /**
     * 验证Numerical
     *
     * @param clazz
     * @param multiNumber
     */
    public static void testNumerical(Class<? extends AggregateFunction> clazz, boolean multiNumber) {
        Numerical numerical = clazz.getAnnotation(Numerical.class);
        assertNotNull(numerical);
        assertEquals(multiNumber, numerical.multiNumber());
    }

    /**
     * 验证Objective注解
     *
     * @param clazz
     * @param keyStrategy
     * @param retainStrategy
     */
    public static void testObjective(Class<? extends AggregateFunction> clazz, int keyStrategy, int retainStrategy) {
        Objective objective = clazz.getAnnotation(Objective.class);
        assertNotNull(objective);
        assertEquals(keyStrategy, objective.keyStrategy());
        assertEquals(retainStrategy, objective.retainStrategy());
    }

    /**
     * 验证Collective注解
     *
     * @param clazz
     * @param keyStrategy
     * @param retainStrategy
     */
    public static void testCollective(Class<? extends AggregateFunction> clazz, int keyStrategy, int retainStrategy) {
        Collective collective = clazz.getAnnotation(Collective.class);
        assertNotNull(collective);
        assertEquals(keyStrategy, collective.keyStrategy());
        assertEquals(retainStrategy, collective.retainStrategy());
    }

    /**
     * 验证MapType注解
     *
     * @param clazz
     */
    public static void testMapType(Class<? extends AggregateFunction> clazz) {
        MapType mapType = clazz.getAnnotation(MapType.class);
        assertNotNull(mapType);
    }

    /**
     * 验证Mix注解
     *
     * @param clazz
     */
    public static void testMix(Class<? extends AggregateFunction> clazz) {
        Mix mix = clazz.getAnnotation(Mix.class);
        assertNotNull(mix);
    }

    /**
     * 验证聚合函数有空参构造函数
     *
     * @param clazz
     */
    @SneakyThrows
    public static void testNoArgsConstructor(Class<? extends AggregateFunction> clazz) {
        Constructor<? extends AggregateFunction> constructor = clazz.getConstructor();
        AggregateFunction aggregateFunction = constructor.newInstance();
        assertNotNull(aggregateFunction);
    }

}
