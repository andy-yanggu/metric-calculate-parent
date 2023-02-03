package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.number.CubeZero;

@Numerical
@MergeType("VARS")
public class VarsUnit extends VarpUnit<VarsUnit> {
    private static final long serialVersionUID = 914745812510434980L;

    public VarsUnit() {
        super();
    }

    public VarsUnit(CubeNumber sum) {
        this(CubeDecimal.of(sum));
    }

    public VarsUnit(CubeDecimal sum) {
        super(sum);
    }

    public VarsUnit(CubeDecimal value, long count, CubeDecimal sum) {
        super(value, count, sum);
    }

    /**
     * Merge operation.
     * @param that param
     * @return MergedUnit value
     */
    @Override
    public VarsUnit merge(VarsUnit that) {
        if (that == null) {
            return this;
        }
        CubeLong oldCount = count.fastClone();
        CubeLong thatCount = that.count.fastClone();

        CubeDecimal oldSum = this.sum.fastClone();
        CubeDecimal thatSum = that.sum.fastClone();

        CubeNumber<?> oldAvg = (oldCount.value() == 0L) ? new CubeZero<>() : oldSum.fastClone().divide(oldCount);
        CubeNumber<?> thatAvg = (thatCount.value() == 0L) ? new CubeZero<>() : thatSum.fastClone().divide(thatCount);

        CubeDecimal thisValue = value.fastClone();
        CubeDecimal thatValue = that.value.fastClone();

        count.add(that.count);
        sum.add(that.sum);
        if (count.value() > 0L) {
            CubeDecimal nowAvg = sum.fastClone().divide(count);
            CubeDecimal value = thisValue.multiply(thisValue)
                .multiply(oldCount)
                .add(thatAvg.fastClone().subtract(nowAvg)
                    .multiply(thatSum.add(thatSum).subtract(thatAvg.add(nowAvg).add(nowAvg).multiply(thatCount))))
                .add(thatValue.multiply(thatValue).multiply(thatCount))
                .add(oldAvg.fastClone().subtract(nowAvg)
                    .multiply(oldSum.add(oldSum).subtract(oldAvg.add(nowAvg).add(nowAvg).multiply(oldCount))))
                .divide(count);
            this.value = value.value(Math.sqrt(value.doubleValue()));
        }
        return this;
    }

    /**
     * FastClone VarsUnit.
     * @return VarsUnit
     */
    @Override
    public VarsUnit fastClone() {
        return new VarsUnit(value.fastClone(), count.value(), sum.fastClone());
    }
}

