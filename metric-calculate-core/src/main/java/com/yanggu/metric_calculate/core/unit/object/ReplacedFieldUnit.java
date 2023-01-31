package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@MergeType("REPLACEDFIELD")
@Objective(useCompareField = false, retainObject = false)
public class ReplacedFieldUnit<T> extends ReplacedObjectUnit<T> {

    public ReplacedFieldUnit(T value) {
        this.value = value;
    }

    /**
     * fastClone.
     *
     * @return ReplacedUnit
     */
    @Override
    public ReplacedFieldUnit fastClone() {
        return new ReplacedFieldUnit(
                (this.value instanceof Cloneable2) ? ((Cloneable2) this.value).fastClone() : this.value);
    }

}
