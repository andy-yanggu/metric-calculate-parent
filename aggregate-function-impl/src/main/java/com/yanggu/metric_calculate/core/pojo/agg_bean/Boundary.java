package com.yanggu.metric_calculate.core.pojo.agg_bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连续类递增、递减类累加器
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
public class Boundary<T extends Number> {

    /**
     * 头
     */
    private T head;

    /**
     * 尾
     */
    private T tail;

    /**
     * 具体的值
     */
    private Integer value;

}
