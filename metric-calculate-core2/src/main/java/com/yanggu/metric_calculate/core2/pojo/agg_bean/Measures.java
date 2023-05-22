package com.yanggu.metric_calculate.core2.pojo.agg_bean;

import lombok.Data;

@Data
public class Measures {

    /**
     * 数组元素个数
     */
    private Integer count = 0;

    /**
     * 数组中各元素之和
     */
    private Double sum = 0.0D;

    /**
     * 值方差
     */
    private Double variance = 0.0D;

    /**
     * 数组的平均值
     */
    //private Double avg;

}