package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@MergeType("OCCUPIEDFIELD")
@Objective(useCompareField = false, retainObject = false)
public class OccupiedFieldUnit<T extends Cloneable2<T>> extends OccupiedObjectUnit<T> {

    public OccupiedFieldUnit(T value) {
        this.value = value;
    }

    @Override
    public OccupiedFieldUnit<T> fastClone() {
        return new OccupiedFieldUnit<>(value.fastClone());
    }

}
