package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@Objective(useCompareField = false, retainObject = false)
@MergeType(value = "OCCUPIEDFIELD", timeWindowType = TIME_SLIDING_WINDOW)
public class OccupiedFieldUnit<T extends Cloneable2<T>> extends OccupiedObjectUnit<T> {

    public OccupiedFieldUnit(T value) {
        this.value = value;
    }

    @Override
    public OccupiedFieldUnit<T> fastClone() {
        return new OccupiedFieldUnit<>(value.fastClone());
    }

}
