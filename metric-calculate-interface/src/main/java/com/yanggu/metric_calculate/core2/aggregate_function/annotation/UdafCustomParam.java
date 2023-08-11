package com.yanggu.metric_calculate.core2.aggregate_function.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * udaf函数参数注解
 * <p>当该注解修饰某个字段, 标识该字段为udaf参数</p>
 * <p>参数名为定义的代码定义的字段名</p>
 * <p>默认值为代码赋值的默认值</p>
 * <p>数据类型为代码定义的数据类型</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UdafCustomParam {

    /**
     * 描述信息
     *
     * @return
     */
    String description() default "";

    /**
     * 能够修改
     * true能够修改, false不能修改
     *
     * @return
     */
    boolean update() default false;

    /**
     * 是否必填
     *
     * @return
     */
    boolean notNull() default false;

}
