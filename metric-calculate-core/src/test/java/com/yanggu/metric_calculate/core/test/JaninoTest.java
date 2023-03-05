package com.yanggu.metric_calculate.core.test;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.value.Key;
import org.codehaus.janino.*;
import org.codehaus.janino.Scanner;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JaninoTest {

    private int count = 10000_0000;

    /**
     * 简单测试Janino表达式的性能
     *
     * @throws Exception
     */
    //@RepeatedTest(5)
    public void test1() throws Exception {
        ScriptEvaluator evaluator = new ScriptEvaluator();
        String expression = FileUtil.readUtf8String("test_janino");
        String[] parameterNames = {"param", "initValue"};
        Class<?>[] parameterTypes = {Map.class, Object.class};
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setReturnType(Object.class);
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
    //@RepeatedTest(5)
    public void test2() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("limit", 10);
            ListObjectUnit<Key<Integer>> listObjectUnit;
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
    //@RepeatedTest(5)
    public void test3() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("limit", 10);
            ListObjectUnit<Key<Integer>> listObjectUnit;
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

    @Test
    public void test4() throws Exception {
        ExpressionEvaluator ee = new ExpressionEvaluator();

        // The expression will have two "int" parameters: "a" and "b".
        ee.setParameters(new String[]{"a", "b"}, new Class[]{int.class, int.class});

        // And the expression (i.e. "result") type is also "int".
        ee.setExpressionType(int.class);

        // And now we "cook" (scan, parse, compile and load) the fabulous expression.
        ee.cook("a + b");

        int result = (Integer) ee.evaluate(new Object[]{12, 23});
        Assert.assertEquals(35, result);
    }

    @Test
    public void test5() throws Exception {
        String[] strings = ExpressionEvaluator.guessParameterNames(new Scanner(null, new StringReader("a + b")));
        System.out.println(Arrays.toString(strings));
        Set<String> parameterNames = new HashSet<>(
                Arrays.asList(ExpressionEvaluator.guessParameterNames(new Scanner(null, new StringReader(
                        ""
                                + "import o.p;\n"
                                + "a + b.c + d.e() + f() + g.h.I.j() + k.l.M"
                ))))
        );
        Assert.assertEquals(new HashSet<>(Arrays.asList("a", "b", "d")), parameterNames);

        parameterNames = new HashSet<>(
                Arrays.asList(ScriptEvaluator.guessParameterNames(new Scanner(null, new StringReader(
                        ""
                                + "import o.p;\n"
                                + "int a;\n"
                                + "return a + b.c + d.e() + f() + g.h.I.j() + k.l.M;"
                ))))
        );
        Assert.assertEquals(new HashSet<>(Arrays.asList("b", "d")), parameterNames);
    }

    @Test
    public void test6() throws Exception {
        String express = "x * x - x";
        Parser parser = new Parser(new Scanner(null, new StringReader(express)));
        Java.Rvalue rvalue = parser.parseExpression();
        System.out.println(rvalue);
    }

}
