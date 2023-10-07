package com.yanggu.metric_calculate.core.window;

import com.yanggu.metric_calculate.core.enums.WindowTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 窗口注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WindowAnnotation {

    /**
     * 窗口类型枚举
     *
     * @return
     */
    WindowTypeEnum type();

    /**
     * 窗口能够合并
     * <p>true能够合并, </p>
     * <p>默认能够合并</p>
     *
     * @return
     */
    boolean canMerge() default true;

}
