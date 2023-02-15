package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@MergeType(value = "MAXFIELD", useParam = true)
@Objective(useCompareField = true, retainObject = false)
public class MaxFieldUnit<T extends Comparable<T> & Cloneable2<T>> extends MaxObjectUnit<T> {

    public MaxFieldUnit(Map<String, Object> params) {
        super(params);
    }

    public MaxFieldUnit(T o) {
        setValue(o);
    }

    @Override
    public MaxFieldUnit<T> fastClone() {
        return new MaxFieldUnit<>(this.maxValue.fastClone());
    }

}