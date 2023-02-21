package com.yanggu.metric_calculate.core.test;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.value.Key;
import org.codehaus.janino.ScriptEvaluator;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JaninoTest {

    private int count = 10000_0000;

    /**
     * 简单测试Janino表达式的性能
     *
     * @throws Exception
     */
    @RepeatedTest(20)
    public void test1() throws Exception {
        //String testJarPath = UnitFactoryTest.testJarPath();
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList("D:\\project\\self\\metric-calculate\\udaf-test\\target\\udaf-test-1.0.0-SNAPSHOT.jar"));

        ScriptEvaluator evaluator = new ScriptEvaluator();
        String expression = FileUtil.readUtf8String("test_janino");
        String[] parameterNames = {"param", "initValue"};
        Class<?>[] parameterTypes = {Map.class, Object.class};
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setReturnType(Object.class);
        evaluator.setParentClassLoader(unitFactory.getUrlClassLoader());
        evaluator.cook(expression);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("limit", 10);
            Object evaluate = evaluator.evaluate(param, new Key<>(i));
        }
        stopWatch.stop();
        System.out.println("Janino表达式执行: " + count + "次总共耗费" + stopWatch.getTotal(TimeUnit.MILLISECONDS) + "毫秒");
    }

    /**
     * 测试反射性能
     *
     * @throws Exception
     */
    @RepeatedTest(20)
    public void test2() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("limit", 10);
            ListObjectUnit listObjectUnit;
            if (CollUtil.isNotEmpty(param)) {
                listObjectUnit = ListObjectUnit.class.getConstructor(Map.class).newInstance(param);
            } else {
                listObjectUnit = ListObjectUnit.class.getConstructor().newInstance();
            }
            listObjectUnit.add(new Key<>(i));
        }
        stopWatch.stop();
        System.out.println("反射执行: " + count + "总共耗费" + stopWatch.getTotal(TimeUnit.MILLISECONDS) + "毫秒");
    }

    /**
     * 使用原生java代码
     */
    @RepeatedTest(20)
    public void test3() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("limit", 10);
            ListObjectUnit listObjectUnit;
            if (CollUtil.isEmpty(param)) {
                listObjectUnit = new ListObjectUnit<>();
            } else {
                listObjectUnit = new ListObjectUnit<>(param);
            }
            listObjectUnit.add(new Key<>(i));
        }
        stopWatch.stop();
        System.out.println("使用原生java代码执行: " + count + "次总共耗费" + stopWatch.getTotal(TimeUnit.MILLISECONDS) + "毫秒");
    }

}
