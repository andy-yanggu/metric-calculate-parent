package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeNumber;

@MergeType("MIN")
@Numerical
public class MinUnit<N extends CubeNumber<N>> extends NumberUnit<N, MinUnit<N>> {
    private static final long serialVersionUID = -3661833176927111287L;

    public MinUnit() {
        super(null, 0L);
    }

    public MinUnit(N value) {
        super(value, 1L);
    }

    public MinUnit(N value, long count) {
        super(value, count);
    }

    @Override
    public MinUnit<N> merge(MinUnit<N> that) {
        if (that == null) {
            return this;
        }
        count.add(that.count);
        setValue(CubeNumber.min(value, that.value));
        return this;
    }

    @Override
    public MinUnit<N> fastClone() {
        return new MinUnit<>(value.fastClone(), count.value());
    }
}
