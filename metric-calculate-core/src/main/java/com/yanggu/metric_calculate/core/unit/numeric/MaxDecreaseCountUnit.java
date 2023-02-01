package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;

@Numerical
@MergeType("MAXDECREASECOUNT")
public class MaxDecreaseCountUnit extends DecreaseCountUnit<MaxDecreaseCountUnit> {

    private static final long serialVersionUID = -5069152371923805623L;

    public long headCount = 0L;

    public long tailCount = 0L;

    public MaxDecreaseCountUnit() {
    }

    public MaxDecreaseCountUnit(CubeNumber center) {
        super(center);
    }

    /**
     * Constructor.
     */
    public MaxDecreaseCountUnit(CubeNumber head, CubeNumber tail,
                                  CubeLong value, long count,
                                  long headCount, long tailCount) {
        super(head, tail, value, count);
        this.headCount = headCount;
        this.tailCount = tailCount;
    }

    private void internalMergeOp(MaxDecreaseCountUnit that) {
        CubeLong oldValue = this.value;
        CubeLong thatValue = that.value;
        value = CubeNumber.max(oldValue, thatValue);
        long tailCount = 0;
        if (that.headCount == thatValue.value()) {
            if (that.head.doubleValue() < this.tail.doubleValue()) {
                value.value(Math.max(value.longValue(), that.headCount + this.tailCount + 1L));
                if (that.count.value() - that.tailCount <= 1L) {
                    tailCount = that.tailCount + this.tailCount + 1L;
                }
                if (this.count.value() - this.headCount <= 1L) {
                    this.headCount = this.headCount + that.headCount + 1L;
                }
            }
        }
        this.tail = that.tail;
        this.tailCount = Math.max(that.tailCount, tailCount);
        this.count.add(that.count);
    }

    /**
     * Merge Operation.
     * @param that input Args
     * @return MergedUnit Object
     */
    @Override
    public MaxDecreaseCountUnit merge(MaxDecreaseCountUnit that) {
        if (that == null) {
            return this;
        }
        internalMergeOp(that);
        return this;
    }

    /**
     * fastClone.
     * @return MaxDecreaseCountUnit
     */
    @Override
    public MaxDecreaseCountUnit fastClone() {
        MaxDecreaseCountUnit maxDecreaseCountNumber = new MaxDecreaseCountUnit(
                this.head, this.tail, this.value, this.count.value(), this.headCount, this.tailCount);
        return maxDecreaseCountNumber;
    }

    /**
     * Equal or Not.
     * @param other input param
     * @return true or false
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
        MaxDecreaseCountUnit maxDecreaseCountNumber = (MaxDecreaseCountUnit) other;
        if (this.headCount != maxDecreaseCountNumber.headCount) {
            return false;
        }
        if (this.tailCount != maxDecreaseCountNumber.tailCount) {
            return false;
        }
        return super.equals(other);
    }
}
