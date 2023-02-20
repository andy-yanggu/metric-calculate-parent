package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@Objective(useCompareField = false, retainObject = false)
@MergeType(value = "OCCUPIEDFIELD", timeWindowType = TIME_SLIDING_WINDOW)
public class OccupiedFieldUnit<T extends Cloneable2<T>> implements ObjectiveUnit<T, OccupiedFieldUnit<T>>, Value<T> {

    private OccupiedObjectUnit<T> occupiedObjectUnit = new OccupiedObjectUnit<>();

    public OccupiedFieldUnit(T value) {
        this.occupiedObjectUnit.setValue(value);
    }

    @Override
    public OccupiedFieldUnit<T> merge(OccupiedFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.occupiedObjectUnit.merge(that.occupiedObjectUnit);
        return this;
    }

    @Override
    public OccupiedFieldUnit<T> fastClone() {
        OccupiedFieldUnit<T> occupiedFieldUnit = new OccupiedFieldUnit<>();
        occupiedFieldUnit.occupiedObjectUnit = this.occupiedObjectUnit.fastClone();
        return occupiedFieldUnit;
    }

    @Override
    public OccupiedFieldUnit<T> value(T object) {
        this.occupiedObjectUnit.value(object);
        return this;
    }

    @Override
    public T value() {
        return this.occupiedObjectUnit.value();
    }

    public T getValue() {
        return this.occupiedObjectUnit.getValue();
    }

    public void setValue(T value) {
        this.occupiedObjectUnit.setValue(value);
    }

    @Override
    public int hashCode() {
        return occupiedObjectUnit != null ? occupiedObjectUnit.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OccupiedFieldUnit<?> that = (OccupiedFieldUnit<?>) o;

        return Objects.equals(occupiedObjectUnit, that.occupiedObjectUnit);
    }

}
