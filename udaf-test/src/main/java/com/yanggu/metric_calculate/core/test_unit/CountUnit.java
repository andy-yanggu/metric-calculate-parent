package com.yanggu.metric_calculate.core.test_unit;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;

/**
 * 数值型不使用自定义参数
 */
@MergeType("COUNT2")
@Numerical
public class CountUnit extends NumberUnit<CubeLong, CountUnit> {

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
        return new CountUnit(value.fastClone(), count.longValue());
    }

}

