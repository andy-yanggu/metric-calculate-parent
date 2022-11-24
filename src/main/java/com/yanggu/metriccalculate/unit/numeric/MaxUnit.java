/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.numeric;

import com.yanggu.metriccalculate.annotation.Numerical;
import com.yanggu.metriccalculate.number.CubeNumber;

@Numerical
public class MaxUnit<N extends CubeNumber<N>> extends NumberUnit<N, MaxUnit<N>> {
    private static final long serialVersionUID = -3661833176927111287L;

    public MaxUnit() {
        super(null, 0L);
    }

    public MaxUnit(N value) {
        super(value, 1L);
    }

    public MaxUnit(N value, long count) {
        super(value, count);
    }

    /**
     * MergeOperation.
     * @param that parameter
     * @return MergedUnit
     */
    @Override
    public MaxUnit<N> merge(MaxUnit<N> that) {
        if (that == null) {
            return this;
        }
        if (getClass().equals(that.getClass())) {
            MaxUnit<N> maxUnit = that;
            count.add(maxUnit.count);
            setValue(CubeNumber.max(value, maxUnit.value));
        }
        return this;
    }

    /**
     * FastClone.
     * @return MaxUnit
     */
    @Override
    public MaxUnit<N> fastClone() {
        MaxUnit<N> maxUnit = new MaxUnit<>(value.fastClone(), count.value());
        return maxUnit;
    }
}
