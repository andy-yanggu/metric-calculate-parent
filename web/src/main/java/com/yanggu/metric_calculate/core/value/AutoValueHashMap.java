package com.yanggu.metric_calculate.core.value;

import java.util.HashMap;

public class AutoValueHashMap extends HashMap<Object, Object> {
    private static final long serialVersionUID = -6848636637050047448L;

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        return value instanceof Value ? ((Value) value).value() : value;
    }

    public Object getRaw(Object key) {
        return super.get(key);
    }
}
