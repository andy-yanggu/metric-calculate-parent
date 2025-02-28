package com.yanggu.metric_calculate.core.function_factory;

import com.yanggu.metric_calculate.core.util.TestJarUtil;


public class AviatorFunctionFactoryBase {

    public static final AviatorFunctionFactory AVIATOR_FUNCTION_FACTORY;

    static {
        AVIATOR_FUNCTION_FACTORY = new AviatorFunctionFactory(TestJarUtil.testJarPath());
        try {
            AVIATOR_FUNCTION_FACTORY.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}