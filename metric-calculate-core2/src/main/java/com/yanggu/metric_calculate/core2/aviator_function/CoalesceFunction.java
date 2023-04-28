package com.yanggu.metric_calculate.core2.aviator_function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * 自定义Aviator函数
 * <p>返回第一个不为null的值, 如果都为null, 则返回nil</p>
 */
public class CoalesceFunction extends AbstractVariadicFunction {

    @Override
    public String getName() {
        return "coalesce";
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
