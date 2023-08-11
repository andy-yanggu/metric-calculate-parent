package com.yanggu.metric_calculate.core2.aviator_function_test;


import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.yanggu.metric_calculate.core2.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionName;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.RoundingMode.UP;

@AviatorFunctionName("test_add")
public class TestAddFunction extends AbstractUdfAviatorFunction {

    private Integer length;

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Number left = FunctionUtils.getNumberValue(args[0], env);
        Number right = FunctionUtils.getNumberValue(args[1], env);
        return new AviatorDecimal(BigDecimal.valueOf(left.doubleValue() + right.doubleValue()).setScale(length, UP));
    }

}
