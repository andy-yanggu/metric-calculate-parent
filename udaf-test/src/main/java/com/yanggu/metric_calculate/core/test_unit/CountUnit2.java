package com.yanggu.metric_calculate.core.test_unit;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;

@MergeType("COUNT2")
@Numerical
public class CountUnit2 extends NumberUnit<CubeLong, CountUnit2> {

    public CountUnit2() {
        super();
    }

    public CountUnit2(CubeNumber value) {
        this(value instanceof CubeLong ? (CubeLong) value : CubeLong.of(value));
    }

    public CountUnit2(CubeLong value) {
        super(value, value.fastClone());
    }

    public CountUnit2(CubeLong value, long count) {
        super(value, count);
    }

    @Override
    public Number value() {
        return (this.value == null) ? 0L : this.value.longValue();
    }

    @Override
    public CountUnit2 merge(CountUnit2 that) {
        if (that == null) {
            return this;
        }
        value.add(that.value);
        count.add(that.count);
        return this;
    }

    @Override
    public CountUnit2 fastClone() {
        return new CountUnit2(value.fastClone(), count.longValue());
    }

}

