package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;

public class CountUnit extends NumberUnit<CubeLong, CountUnit> {
    private static final long serialVersionUID = -8427535031107918740L;

    public CountUnit() {
        super();
    }

    public CountUnit(CubeNumber value) {
        this(value instanceof CubeLong ? (CubeLong) value : CubeLong.of(value));
    }

    public CountUnit(CubeLong value) {
        super(value, value.fastClone());
    }

    public CountUnit(CubeLong value, long count) {
        super(value, count);
    }

    @Override
    public Number value() {
        return (this.value == null) ? 0L : this.value.longValue();
    }

    @Override
    public CountUnit merge(CountUnit that) {
        if (that == null) {
            return this;
        }
        value.add(that.value);
        count.add(that.count);
        return this;
    }

    @Override
    public CountUnit fastClone() {
        CountUnit countUnit = new CountUnit(value.fastClone(), count.longValue());
        return countUnit;
    }
}

