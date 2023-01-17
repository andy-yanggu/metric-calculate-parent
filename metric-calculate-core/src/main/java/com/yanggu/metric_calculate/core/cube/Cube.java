package com.yanggu.metric_calculate.core.cube;

public interface Cube<C extends Cube> {

    String getName();

    void setName(String name);

    String getKey();

    void setKey(String key);

    boolean isEmpty();

    C cloneEmpty();

    Cube init();

}