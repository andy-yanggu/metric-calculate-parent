package com.yanggu.metriccalculate.cube;

public interface Cube<C extends Cube> {

    String name();

    String key();

    boolean isEmpty();

    C cloneEmpty();

    Cube init();

}