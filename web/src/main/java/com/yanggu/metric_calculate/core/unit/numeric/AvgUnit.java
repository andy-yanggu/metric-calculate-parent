package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;

public class AvgUnit<N extends CubeNumber<N>> extends NumberUnit<N, AvgUnit<N>> {
    private static final long serialVersionUID = 2047476248685393133L;

    public AvgUnit() {
        super();
    }

    public AvgUnit(N value) {
        super(value, (value == null) ? CubeLong.of(0L) : CubeLong.of(1L));
    }

    public AvgUnit(N value, long count) {
        super(value, CubeLong.of(count));
    }

    @Override
    public AvgUnit<N> merge(AvgUnit<N> that) {
        if (that == null) {
            return this;
        }
        value.multiply(count).add(that.value.multiply(that.count)).divide(count.add(that.count));
        return this;
    }

    @Override
    public AvgUnit<N> fastClone() {
        return new AvgUnit<>(value.fastClone(), count.longValue());
    }

}
