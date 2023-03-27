package com.yanggu.metric_calculate.core2.aviator_function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 自定义Aviator函数
 * <p>返回第一个不为null的值, 如果都为null, 则返回nil</p>
 * <p>只接受数值类型的参数</p>
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
            if (value instanceof Number) {
                return AviatorNumber.valueOf(value);
            } else if (value != null) {
                throw new RuntimeException("传入的数据不是数值类型的" + value);
            }
        }
        return AviatorNil.NIL;
    }

}
