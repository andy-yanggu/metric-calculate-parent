package com.yanggu.metric_calculate.core.aviator_function;


import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.yanggu.metric_calculate.core.aviator_function.annotation.AviatorFunctionAnnotation;

import java.io.Serial;

/**
 * 用户自定义AviatorFunction
 * <p>需要继承该抽象类重写相应的方法</p>
 * <p>同时需要{@link AviatorFunctionAnnotation}标注</p>
 */
public abstract class AbstractUdfAviatorFunction extends AbstractVariadicFunction {

    @Serial
    private static final long serialVersionUID = 2042192208322795369L;

    /**
     * 初始化方法
     * <p>反射调用空参构造实例化对象</p>
     * <p>给成员变量反射赋值之后, 会调用init方法</p>
     */
    public void init() {
        AviatorFunctionAnnotation annotation = this.getClass().getAnnotation(AviatorFunctionAnnotation.class);
        if (annotation == null) {
            throw new RuntimeException("自定义函数必须有AviatorFunctionName注解");
        }
    }

    /**
     * 获取子类类名上的注解中的名称
     *
     * @return
     */
    @Override
    public String getName() {
        AviatorFunctionAnnotation annotation = this.getClass().getAnnotation(AviatorFunctionAnnotation.class);
        if (annotation == null) {
            throw new RuntimeException("自定义函数必须有AviatorFunctionName注解");
        }
        return annotation.name();
    }

}
