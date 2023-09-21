package com.yanggu.metric_calculate.function_test.aviator_function_test;


import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.yanggu.metric_calculate.core.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionAnnotation;
import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionFieldAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Map;

import static java.math.RoundingMode.UP;

@Data
@AviatorFunctionAnnotation(name = "test_add", displayName = "测试add")
@EqualsAndHashCode(callSuper = true)
public class TestAddFunction extends AbstractUdfAviatorFunction {

    @Serial
    private static final long serialVersionUID = -4770027284173452385L;

    @AviatorFunctionFieldAnnotation(displayName = "长度")
    private Integer length = 10;

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Number left = FunctionUtils.getNumberValue(args[0], env);
        Number right = FunctionUtils.getNumberValue(args[1], env);
        return new AviatorDecimal(BigDecimal.valueOf(left.doubleValue() + right.doubleValue()).setScale(length, UP));
    }

}
