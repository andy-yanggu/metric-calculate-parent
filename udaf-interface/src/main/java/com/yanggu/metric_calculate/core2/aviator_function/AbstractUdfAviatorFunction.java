package com.yanggu.metric_calculate.core2.aviator_function;


import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;

/**
 * 用户自定义AviatorFunction
 */
public abstract class AbstractUdfAviatorFunction extends AbstractVariadicFunction {

    /**
     * 初始化方法
     * <p>反射调用空参构造实例化对象</p>
     * <p>给成员变量反射赋值之后, 会调用init方法</p>
     */
    public void init() {
    }


}
