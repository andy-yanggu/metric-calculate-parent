/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.value;

public class NoneValue implements Value {
    public static final NoneValue INSTANCE = new NoneValue();

    private NoneValue() {
    }

    @Override
    public Object value() {
        return 0;
    }

    @Override
    public String toString() {
        return "None";
    }
}
