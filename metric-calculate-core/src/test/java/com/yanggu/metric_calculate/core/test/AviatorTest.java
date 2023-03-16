package com.yanggu.metric_calculate.core.test;


import cn.hutool.core.date.StopWatch;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.junit.Assert;
import org.junit.Test;

public class AviatorTest {

    //@Test
    public void test1() {
        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
        Expression expression = instance.compile("1 + 1");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("Aviator表达式开始执行");
        for (int i = 0; i < 100000000; i++) {
            Object execute = expression.execute();
            Assert.assertEquals(2L, execute);
        }
        stopWatch.stop();
        System.out.println("Aviator表达式" + stopWatch.getTotalTimeMillis() + "毫秒");
    }

    //@Test
    public void test2() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("Java原生代码开始执行");
        for (int i = 0; i < 100000000; i++) {
            int data = 1 + 1;
            Assert.assertEquals(2, data);
        }
        stopWatch.stop();
        System.out.println("Java原生代码" + stopWatch.getTotalTimeMillis() + "毫秒");
    }

    //@Test
    public void test3() throws Exception {
        ExpressionEvaluator ee = new ExpressionEvaluator();
        ee.cook("1 + 1");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("Janino编译代码开始执行");
        for (int i = 0; i < 100000000; i++) {
            int data = (int) ee.evaluate();;
            Assert.assertEquals(2, data);
        }
        stopWatch.stop();
        System.out.println("Janino编译代码" + stopWatch.getTotalTimeMillis() + "毫秒");
    }

}
