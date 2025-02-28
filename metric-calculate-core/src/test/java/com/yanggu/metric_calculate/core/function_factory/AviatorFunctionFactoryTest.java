package com.yanggu.metric_calculate.core.function_factory;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aviator_function.AbstractUdfAviatorFunction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactoryBase.AVIATOR_FUNCTION_FACTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 自定义Aviator函数单元测试类
 */
@DisplayName("自定义Aviator函数单元测试类")
class AviatorFunctionFactoryTest {

    @Test
    void test1() {
        AbstractUdfAviatorFunction aviatorFunction = AVIATOR_FUNCTION_FACTORY.initAviatorFunction("coalesce", null);
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
        AbstractUdfAviatorFunction aviatorFunction = AVIATOR_FUNCTION_FACTORY.initAviatorFunction("test_add", params);
        AviatorEvaluator.addFunction(aviatorFunction);
        Expression expression = AviatorEvaluator.compile("test_add(1, 2)");
        Object execute = expression.execute();
        assertEquals(new BigDecimal("3.00"), execute);
    }

}