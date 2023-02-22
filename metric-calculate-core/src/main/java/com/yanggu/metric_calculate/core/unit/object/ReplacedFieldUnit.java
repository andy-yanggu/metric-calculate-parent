package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@Data
@Objective(useCompareField = false, retainObject = false)
@MergeType(value = "REPLACEDFIELD", timeWindowType = TIME_SLIDING_WINDOW)
public class ReplacedFieldUnit<T extends Clone<T>> implements ObjectiveUnit<T, ReplacedFieldUnit<T>>, Value<T> {

    private ReplacedObjectUnit<T> replacedObjectUnit;

    public ReplacedFieldUnit() {
        this.replacedObjectUnit = new ReplacedObjectUnit<>();
    }

    public ReplacedFieldUnit(T value) {
        this();
        value(value);
    }

    @Override
    public ReplacedFieldUnit<T> value(T object) {
        this.replacedObjectUnit.value(object);
        return this;
    }

    @Override
    public ReplacedFieldUnit<T> merge(ReplacedFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.replacedObjectUnit.merge(that.replacedObjectUnit);
        return this;
    }

    @Override
    public T value() {
        return this.replacedObjectUnit.value();
    }

    /**
     * fastClone.
     *
     * @return ReplacedUnit
     */
    @Override
    public ReplacedFieldUnit<T> fastClone() {
        ReplacedFieldUnit<T> replacedFieldUnit = new ReplacedFieldUnit<>();
        replacedFieldUnit.replacedObjectUnit = this.replacedObjectUnit.fastClone();
        return replacedFieldUnit;
    }

}
