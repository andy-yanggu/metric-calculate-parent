package com.yanggu.metric_calculate.core2.pojo.agg_bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Boundary<T extends Number> {

    private T head;

    private T tail;

    private Integer value;

}
