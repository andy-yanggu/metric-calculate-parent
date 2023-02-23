package com.yanggu.metric_calculate.core.value;

public interface Clone<C extends Clone<C>> {

    C fastClone();

}

