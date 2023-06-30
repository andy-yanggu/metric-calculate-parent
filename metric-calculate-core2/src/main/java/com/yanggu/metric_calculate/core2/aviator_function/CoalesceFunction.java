package com.yanggu.metric_calculate.core2.aviator_function;

import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * 自定义Aviator函数
 * <p>返回第一个不为null的值, 如果都为null, 则返回nil</p>
 */
@AviatorFunctionName("coalesce")
public class CoalesceFunction extends AbstractUdfAviatorFunction {

    private static final long serialVersionUID = -8698263751319322038L;

    private static final String NAME = CoalesceFunction.class.getAnnotation(AviatorFunctionName.class).value();

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * 参数的个数不确定, 返回第一个不为null的值
     *
     * @param env
     * @param args
     * @return
     */
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        for (AviatorObject aviatorObject : args) {
            Object value = aviatorObject.getValue(env);
            if (value != null) {
                return AviatorRuntimeJavaType.valueOf(value);
            }
        }
        return AviatorNil.NIL;
    }

}
