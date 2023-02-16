package com.yanggu.metric_calculate.core.cube;

public interface Cube<C extends Cube<C>> {

    C init();

    String getName();

    void setName(String name);

    String getKey();

    void setKey(String key);

    boolean isEmpty();

    C cloneEmpty();

}