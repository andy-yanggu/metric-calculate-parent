package com.yanggu.metric_calculate.core2.pojo.agg_bean;

import lombok.Data;
import lombok.NoArgsConstructor;

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
