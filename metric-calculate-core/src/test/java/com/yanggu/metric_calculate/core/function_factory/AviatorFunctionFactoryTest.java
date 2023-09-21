package com.yanggu.metric_calculate.core.function_factory;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core.util.TestJarUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void test1() {
        AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.initAviatorFunction("coalesce", null);
        AviatorEvaluator.addFunction(aviatorFunction);
        Expression expression = AviatorEvaluator.compile("coalesce(a, b, 1)");
        Map<String, Object> env = new HashMap<>();
        Object execute = expression.execute(env);
        assertEquals(1L, execute);
    }

    @Test
    @Disabled
    void test2() {
        Map<String, Object> params = new HashMap<>();
        params.put("length", 2);
        AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.initAviatorFunction("test_add", params);
        AviatorEvaluator.addFunction(aviatorFunction);
        Expression expression = AviatorEvaluator.compile("test_add(1, 2)");
        Object execute = expression.execute();
        assertEquals(new BigDecimal("3.00"), execute);
    }

}