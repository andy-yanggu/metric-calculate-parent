package com.yanggu.metric_calculate.core2.aviator_function;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 自定义Aviator函数单元测试类
 */
public class AviatorFunctionFactoryTest {

    private static final AviatorFunctionFactory aviatorFunctionFactory;

    static {
        aviatorFunctionFactory = new AviatorFunctionFactory();
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
        AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory();
        aviatorFunctionFactory.init();
        AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.getAviatorFunction("coalesce");
        System.out.println(aviatorFunction);
    }


}