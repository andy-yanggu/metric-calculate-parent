package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Data
@NoArgsConstructor
@FieldNameConstants
@MergeType(value = "MINFIELD", useParam = true)
@Objective(useCompareField = true, retainObject = false)
public class MinFieldUnit<T extends Comparable<T> & Cloneable2<T>> extends MinObjectUnit<T> {

    public MinFieldUnit(T value) {
        setValue(value);
    }

    public MinFieldUnit(Map<String, Object> params) {
        super(params);
    }

    @Override
    public MinFieldUnit<T> fastClone() {
        return new MinFieldUnit<>(value.fastClone());
    }

    @Override
    public String toString() {
        return String.format("%s {value=%s}", getClass().getSimpleName(), this.value);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MinFieldUnit<T> thatUnit = (MinFieldUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }

}
