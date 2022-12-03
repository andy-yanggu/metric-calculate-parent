package com.yanggu.metric_calculate.core.cube;

public interface Cube<C extends Cube> {

    String name();

    String key();

    boolean isEmpty();

    C cloneEmpty();

    Cube init();

}