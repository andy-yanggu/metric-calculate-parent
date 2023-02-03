package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeNumber;

@Numerical
@MergeType("MAX")
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
            count.add(that.count);
            setValue(CubeNumber.max(value, that.value));
        }
        return this;
    }

    /**
     * FastClone.
     * @return MaxUnit
     */
    @Override
    public MaxUnit<N> fastClone() {
        return new MaxUnit<>(value.fastClone(), count.value());
    }
}
