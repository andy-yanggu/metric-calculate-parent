package com.yanggu.metric_calculate.core.value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ValueMapper {

    private ValueMapper() {
    }

    /**
     * Return value string.
     */
    public static Object value(Value<?> value) {
        Object v;
        if (value == null || (v = value.value()) == null) {
            return null;
        }
        if (v instanceof Value) {
            v = value(((Value<?>) v));
        }
        if (v instanceof Collection) {
            v = ((Collection<?>) v).stream()
                    .map(temp -> temp instanceof Value ? ((Value<?>) temp).value() : temp)
                    .collect(Collectors.toList());
        }
        //if (v instanceof Map) {
        //    Map<?, ?> map = (Map<?, ?>) v;
        //    Map<Object, Object> newMap = new HashMap<>();
        //    map.forEach((tempKey, tempValue) -> {
        //        Object newKey = tempKey instanceof Value ? value(((Value<?>) tempKey)) : tempKey;
        //        Object newValue = tempValue instanceof Value ? value(((Value<?>) tempValue)) : tempValue;
        //        newMap.put(newKey, newValue);
        //    });
        //    v = newMap;
        //}
        return v;
    }

}