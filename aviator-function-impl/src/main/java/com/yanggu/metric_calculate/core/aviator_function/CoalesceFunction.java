package com.yanggu.metric_calculate.core.aviator_function;

import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.yanggu.metric_calculate.core.aviator_function.annotation.AviatorFunctionAnnotation;
import org.dromara.hutool.core.array.ArrayUtil;

import java.io.Serial;
import java.util.Map;

/**
 * 自定义Aviator函数
 * <p>返回第一个不为null的值, 如果都为null, 则返回nil</p>
 */
@AviatorFunctionAnnotation(name = "coalesce", displayName = "返回第一个非空函数")
public class CoalesceFunction extends AbstractUdfAviatorFunction {

    @Serial
    private static final long serialVersionUID = -8698263751319322038L;

    /**
     * 参数的个数不确定, 返回第一个不为null的值
     */
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        if (ArrayUtil.isEmpty(args)) {
            return AviatorNil.NIL;
        }
        for (AviatorObject aviatorObject : args) {
            Object value = aviatorObject.getValue(env);
            if (value != null) {
                return AviatorRuntimeJavaType.valueOf(value);
            }
        }
        return AviatorNil.NIL;
    }

}
