package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.number.CubeZero;

@MergeType("VARP")
@Numerical
public class VarpUnit<V extends VarpUnit<V>> extends NumberUnit<CubeDecimal, V> {
    private static final long serialVersionUID = -2932063628549881110L;

    public CubeDecimal sum;

    public VarpUnit() {
        super();
    }

    public VarpUnit(CubeNumber sum) {
        this(CubeDecimal.of(0), 1, CubeDecimal.of(sum));
    }

    public VarpUnit(CubeDecimal sum) {
        this(CubeDecimal.of(0), 1, sum);
    }

    public VarpUnit(CubeDecimal value, long count, CubeDecimal sum) {
        super(value, count);
        this.sum = sum;
    }

    /**
     * merge two object.
     * @param that param
     * @return MergedUnit Object
     */
    @Override
    public V merge(V that) {

        CubeLong oldCount = count.fastClone();
        CubeLong thatCount = that.count.fastClone();

        CubeDecimal oldSum = this.sum.fastClone();
        CubeDecimal thatSum = that.sum.fastClone();

        CubeNumber<?> oldAvg = (oldCount.value() == 0L) ? new CubeZero() : oldSum.fastClone().divide(oldCount);
        CubeNumber<?> thatAvg = (thatCount.value() == 0L) ? new CubeZero() : thatSum.fastClone().divide(thatCount);

        CubeDecimal thisValue = value.fastClone();
        CubeDecimal thatValue = that.value.fastClone();

        count.add(that.count);
        sum.add(that.sum);

        if (count.value() > 0L) {
            CubeDecimal nowAvg = sum.fastClone().divide(count);
            value = thisValue.multiply(oldCount)
                .add(thatValue.multiply(thatCount))
                .add(thatAvg.fastClone().subtract(nowAvg)
                    .multiply(thatSum.add(thatSum).subtract(thatAvg.add(nowAvg).add(nowAvg).multiply(thatCount))))
                .add(oldAvg.fastClone().subtract(nowAvg)
                    .multiply(oldSum.add(oldSum).subtract(oldAvg.add(nowAvg).add(nowAvg).multiply(oldCount))))
                .divide(count);
        }
        return (V) this;
    }

    /**
     * fastClone VarpUnit.
     * @return VarpUnit
     */
    @Override
    public V fastClone() {
        return (V) new VarpUnit(value.fastClone(), count.value(), sum.fastClone());
    }

    @Override
    public String toString() {
        return getClass().getName() + " [count=" + this.count + ", sum=" + this.sum + ", value=" + this.value + "]";
    }

    /**
     * isEqual or Not.
     * @param other input param
     * @return True or False
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        VarpUnit<V> varpUnit = (VarpUnit) other;
        if (this.sum == null) {
            if (varpUnit.sum != null) {
                return false;
            }
        } else if (this.sum.doubleValue() != varpUnit.sum.doubleValue()) {
            return false;
        }
        return super.equals(other);
    }
}
