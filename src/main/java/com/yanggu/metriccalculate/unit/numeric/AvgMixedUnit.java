/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.numeric;



import com.yanggu.metriccalculate.number.CubeLong;
import com.yanggu.metriccalculate.number.CubeNumber;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.unit.MixedUnit;
import com.yanggu.metriccalculate.value.Value;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public class AvgMixedUnit<N extends CubeNumber<N>> implements MixedUnit<AvgMixedUnit<N>>, Value<Number> {

    private CountUnit countUnit;
    private SumUnit<N> sumUnit;

    public AvgMixedUnit() {
    }

    public AvgMixedUnit(CubeLong count, N sum) {
        this.countUnit = new CountUnit(count);
        this.sumUnit = new SumUnit<>(sum);
    }

    @Override
    public AvgMixedUnit<N> mixMerge(AvgMixedUnit<N> mixUnit) {
        if (mixUnit == null) {
            return this;
        }
        countUnit.merge(mixUnit.countUnit);
        sumUnit.merge(mixUnit.sumUnit);
        return this;
    }

    @Override
    public AvgMixedUnit<N> mixMerge(MergedUnit unit) {
        if (unit instanceof SumUnit) {
            sumUnit.merge((SumUnit) unit);
        }
        if (unit instanceof CountUnit) {
            countUnit.merge((CountUnit) unit);
        }
        return this;
    }

    @Override
    public Collection<Class<? extends MergedUnit>> supportUnit() {
        return Arrays.asList(CountUnit.class, SumUnit.class);
    }

    @Override
    public AvgMixedUnit<N> merge(AvgMixedUnit<N> that) {
        return mixMerge(that);
    }

    @Override
    public AvgMixedUnit<N> fastClone() {
        AvgMixedUnit<N> result = new AvgMixedUnit();
        result.countUnit = this.countUnit.fastClone();
        result.sumUnit = this.sumUnit.fastClone();
        return result;
    }

    @Override
    public Number value() {
        return sumUnit.fastClone().divide(countUnit).value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AvgMixedUnit<?> that = (AvgMixedUnit<?>) o;
        return Objects.equals(countUnit, that.countUnit) && Objects.equals(sumUnit, that.sumUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countUnit, sumUnit);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AvgMixedUnit.class.getSimpleName() + "[", "]")
            .add("countUnit=" + countUnit)
            .add("sumUnit=" + sumUnit)
            .toString();
    }
}
