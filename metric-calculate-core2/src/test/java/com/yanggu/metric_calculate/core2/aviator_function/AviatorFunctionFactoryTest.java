package com.yanggu.metric_calculate.core2.aviator_function;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core2.test.TestJarUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 自定义Aviator函数单元测试类
 */
public class AviatorFunctionFactoryTest {

    private static final AviatorFunctionFactory aviatorFunctionFactory;

    static {
        aviatorFunctionFactory = new AviatorFunctionFactory(TestJarUtil.testJarPath());
        try {
            aviatorFunctionFactory.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AviatorFunctionFactory getAviatorFunctionFactory() {
        return aviatorFunctionFactory;
    }

    @Test
    public void test1() throws Exception {
        AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.getAviatorFunction("coalesce");
        AviatorEvaluator.addFunction(aviatorFunction);
        Expression expression = AviatorEvaluator.compile("coalesce(a, b, 1)");
        Map<String, Object> env = new HashMap<>();
        Object execute = expression.execute(env);
        assertEquals(1L, execute);
    }

    @Test
    @Ignore
    public void test2() {
        AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.getAviatorFunction("test_add");
        AviatorEvaluator.addFunction(aviatorFunction);
        Map<String, Object> params = new HashMap<>();
        params.put("length", 2);
        AviatorFunctionFactory.init(aviatorFunction, params);
        Expression expression = AviatorEvaluator.compile("test_add(1, 2)");
        Object execute = expression.execute();
        assertEquals(new BigDecimal("3.00"), execute);
    }

}