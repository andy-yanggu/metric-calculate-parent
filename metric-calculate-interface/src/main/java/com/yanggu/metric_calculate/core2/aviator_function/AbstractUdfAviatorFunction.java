package com.yanggu.metric_calculate.core2.aviator_function;


import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;

/**
 * 用户自定义AviatorFunction
 */
public abstract class AbstractUdfAviatorFunction extends AbstractVariadicFunction {

    private static final long serialVersionUID = 2042192208322795369L;

    /**
     * 初始化方法
     * <p>反射调用空参构造实例化对象</p>
     * <p>给成员变量反射赋值之后, 会调用init方法</p>
     */
    public void init() {
        AviatorFunctionName annotation = this.getClass().getAnnotation(AviatorFunctionName.class);
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
        return this.getClass().getAnnotation(AviatorFunctionName.class).value();
    }

}
