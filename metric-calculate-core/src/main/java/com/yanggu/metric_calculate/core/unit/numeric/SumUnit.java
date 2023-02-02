package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeNumber;

@Numerical
@MergeType("SUM")
public class SumUnit<N extends CubeNumber<N>> extends NumberUnit<N, SumUnit<N>> {
    private static final long serialVersionUID = -1402152321834257837L;

    public SumUnit() {
        super(null, 0L);
    }

    public SumUnit(N value) {
        super(value, (value == null) ? 0L : ((value.doubleValue() < 0.0D) ? -1 : 1));
    }

    public SumUnit(N value, long count) {
        super(value, count);
    }

    @Override
    public SumUnit<N> merge(SumUnit<N> that) {
        if (that == null) {
            return this;
        }
        count.add(that.count);
        value.add(that.value);
        return this;
    }

    @Override
    public SumUnit<N> fastClone() {
        return new SumUnit<>(value.fastClone(), count.value());
    }
}

