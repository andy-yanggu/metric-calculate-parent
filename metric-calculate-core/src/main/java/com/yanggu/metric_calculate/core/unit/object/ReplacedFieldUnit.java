package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@Objective(useCompareField = false, retainObject = false)
@MergeType(value = "REPLACEDFIELD", timeWindowType = TIME_SLIDING_WINDOW)
public class ReplacedFieldUnit<T extends Cloneable2<T>> extends ReplacedObjectUnit<T> {

    public ReplacedFieldUnit(T value) {
        this.value = value;
    }

    /**
     * fastClone.
     *
     * @return ReplacedUnit
     */
    @Override
    public ReplacedFieldUnit<T> fastClone() {
        return new ReplacedFieldUnit<>(value.fastClone());
    }

}
