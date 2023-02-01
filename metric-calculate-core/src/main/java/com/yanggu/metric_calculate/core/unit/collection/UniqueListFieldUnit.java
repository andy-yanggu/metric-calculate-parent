package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Collection;
import java.util.Map;

@NoArgsConstructor
@MergeType(value = "DISTINCTLISTFIELD", useParam = true)
@Collective(useCompareField = true, retainObject = false)
public class UniqueListFieldUnit<T extends Cloneable2<T>> extends UniqueListObjectUnit<T> {

    public UniqueListFieldUnit(Map<String, Object> param) {
        super(param);
    }

    public UniqueListFieldUnit(T value) {
        this();
        add(value);
    }

    public UniqueListFieldUnit(Collection<T> values) {
        this();
        addAll(values);
    }

    /**
     * Construct.
     *
     * @param value input
     * @param limit limitCnt
     */
    public UniqueListFieldUnit(T value, int limit) {
        this();
        add(value);
        this.limit = limit;
    }

    /**
     * Construct.
     */
    public UniqueListFieldUnit(Collection<T> values, int paramCnt) {
        this();
        addAll(values);
        this.limit = paramCnt;
    }

    @Override
    public UniqueListFieldUnit<T> fastClone() {
        UniqueListFieldUnit<T> uniqueListObjectUnit = new UniqueListFieldUnit<>();
        uniqueListObjectUnit.limit = this.limit;
        for (T item : getSet()) {
            uniqueListObjectUnit.getSet().add(item.fastClone());
        }
        return uniqueListObjectUnit;
    }

}
