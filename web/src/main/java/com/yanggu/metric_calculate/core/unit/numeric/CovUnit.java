package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.*;

@MergeType("COV")
@Numerical
public class CovUnit<N extends CubeNumber<N>> extends NumberUnit<N, CovUnit<N>> {
    private static final long serialVersionUID = -3797610254543124206L;

    public N sum1;

    public N sum2;

    public N value2;

    public N value3;

    public CovUnit() {
        super();
    }

    /**
     * Constructor.
     */
    public CovUnit(N sum1, N sum2) {
        super(null);
        this.sum1 = sum1;
        this.sum2 = sum2;
    }

    /**
     * constructor.
     */
    public CovUnit(N value, N sum1, N value2, N sum2, N value3, long count) {
        super(value, count);
        this.sum1 = sum1;
        this.value2 = value2;
        this.sum2 = sum2;
        this.value3 = value3;
    }

    @Override
    public CovUnit<N> merge(CovUnit<N> that) {
        if (that == null) {
            return this;
        }
        CubeLong oldCount = count;
        CubeLong thatCount = that.count;
        N oldSum1 = this.sum1;
        N thatSum1 = that.sum1;
        N oldSum2 = this.sum2;
        N thatSum2 = that.sum2;
        N oldSumAll = oldSum2.fastClone().add(oldSum1);
        N thatSumAll = thatSum2.fastClone().add(thatSum1);
        CubeNumber<?> oldAvg1 = oldCount.value() == 0 ? new CubeZero() : oldSum1.fastClone().divide(oldCount);
        CubeNumber<?> thatAvg1 = thatCount.value() == 0 ? new CubeZero() : thatSum1.fastClone().divide(thatCount);
        CubeNumber<?> oldAvg2 = oldCount.value() == 0 ? new CubeZero() : oldSum2.fastClone().divide(oldCount);
        CubeNumber<?> thatAvg2 = thatCount.value() == 0 ? new CubeZero() : thatSum2.fastClone().divide(thatCount);
        CubeNumber<?> oldAvgAll = oldCount.value() == 0 ? new CubeZero() : oldSumAll.fastClone().divide(oldCount);
        CubeNumber<?> thatAvgAll =
            thatCount.value() == 0 ? new CubeZero() : thatSumAll.fastClone().divide(thatCount);
        CubeNumber<?> oldVarpValue1 = varpValue1();
        CubeNumber<?> thatVarpValue1 = that.varpValue1();
        CubeNumber<?> oldVarpValue2 = varpValue2();
        CubeNumber<?> thatVarpValue2 = that.varpValue2();
        CubeNumber<?> oldVarpValue3 = varpValue3();
        CubeNumber<?> thatVarpvalue3 = that.varpValue3();
        sum1.add(thatSum1);
        sum2.add(thatSum2);

        if (count.value() > 0L) {
            N nowAvg1 = oldSum1.fastClone().add(thatSum1).divide(count);
            this.value =
                oldSum1.fastClone().add(oldSum1)
                    .subtract(oldAvg1.fastClone().add(nowAvg1).multiply(oldCount))
                    .multiply(oldAvg1.fastClone().subtract(nowAvg1))
                .add(thatSum1.fastClone().add(thatSum1)
                    .subtract(thatAvg1.fastClone().add(nowAvg1).multiply(thatCount))
                    .multiply(thatAvg1.fastClone().subtract(nowAvg1)))
                .add(thatVarpValue1.fastClone().multiply(thatCount))
                .add(oldVarpValue1.fastClone().multiply(oldCount));
            N nowAvg2 = oldSum2.fastClone().add(thatSum2).divide(count);
            this.value2 =
                oldSum2.fastClone().add(oldSum2)
                    .subtract(oldAvg2.fastClone().add(nowAvg2).multiply(oldCount))
                    .multiply(oldAvg2.fastClone().subtract(nowAvg2))
                .add(thatSum2.fastClone().add(thatSum2)
                    .subtract(thatAvg2.fastClone().add(nowAvg2).multiply(thatCount))
                    .multiply(thatAvg2.fastClone().subtract(nowAvg2)))
                .add(thatVarpValue2.fastClone().multiply(thatCount))
                .add(oldVarpValue2.fastClone().multiply(oldCount));

            N nowAvgAll = oldSumAll.fastClone().add(thatSumAll).divide(count);
            this.value3 =
                oldSumAll.fastClone().add(oldSumAll)
                    .subtract(oldAvgAll.fastClone().add(nowAvgAll).multiply(oldCount))
                    .multiply(oldAvgAll.fastClone().subtract(nowAvgAll))
                .add(thatSumAll.fastClone().add(thatSumAll)
                    .subtract(thatAvgAll.fastClone().add(nowAvgAll).multiply(thatCount))
                    .multiply(thatAvgAll.fastClone().subtract(nowAvgAll)))
                .add(thatVarpvalue3.fastClone().multiply(thatCount))
                .add(oldVarpValue3.fastClone().multiply(oldCount));
        }
        return this;
    }

    /**
     * Correl.
     * @return correl value
     */
    public N correl() {
        double d1 = Math.sqrt(varpValue1().doubleValue());
        double d2 = Math.sqrt(varpValue2().doubleValue());
        return value.fastClone().divide(CubeDouble.of(d1)).multiply(CubeDouble.of(d2));
    }

    @Override
    public Number value() {
        return (Number) varpValue3().subtract(varpValue2()).subtract(varpValue1()).divide(CubeInteger.of(2)).value();
    }


    public CubeNumber<?> varpValue1() {
        return (this.value == null) ? new CubeZero() : this.value.fastClone();
    }

    public CubeNumber<?> varpValue2() {
        return (this.value2 == null) ? new CubeZero() : this.value2.fastClone();
    }

    public CubeNumber<?> varpValue3() {
        return (this.value3 == null) ? new CubeZero() : this.value3.fastClone();
    }

    @Override
    public CovUnit<N> fastClone() {
        CovUnit<N> covUnit = new CovUnit(
            varpValue1(),
            sum1.fastClone(),
            varpValue2(),
            sum2,
            varpValue3(),
            count.longValue());
        return covUnit;
    }

    @Override
    public String toString() {
        return String.format("%s [count=%s, sum1=%s, value1=%s, sum2=%s, value2=%s, value3=%s]",
            getClass().getSimpleName(), this.count, this.sum1, this.value, this.sum2, this.value2, this.value3);
    }

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
        CovUnit<N> covUnit = (CovUnit) other;
        if (this.sum1 == null) {
            if (covUnit.sum1 != null) {
                return false;
            }
        } else if (this.sum1.doubleValue() != covUnit.sum1.doubleValue()) {
            return false;
        }
        if (this.sum2 == null) {
            if (covUnit.sum2 != null) {
                return false;
            }
        } else if (this.sum2.doubleValue() != covUnit.sum2.doubleValue()) {
            return false;
        }
        if (this.value2 == null) {
            if (covUnit.value2 != null) {
                return false;
            }
        } else if (this.value2.doubleValue() != covUnit.value2.doubleValue()) {
            return false;
        }
        if (this.value3 == null) {
            if (covUnit.value3 != null) {
                return false;
            }
        } else if (this.value3.doubleValue() != covUnit.value3.doubleValue()) {
            return false;
        }
        return super.equals(other);
    }

}
