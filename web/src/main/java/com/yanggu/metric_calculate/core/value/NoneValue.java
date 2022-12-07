package com.yanggu.metric_calculate.core.value;

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
