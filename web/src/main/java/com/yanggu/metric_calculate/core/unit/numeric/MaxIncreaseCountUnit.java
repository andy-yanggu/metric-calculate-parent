package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;

public class MaxIncreaseCountUnit extends IncreaseCountUnit<MaxIncreaseCountUnit> {
    private static final long serialVersionUID = -5871264256583642034L;

    public long headCount = 0L;

    public long tailCount = 0L;

    public MaxIncreaseCountUnit() {
    }

    public MaxIncreaseCountUnit(CubeNumber center) {
        super(center);
    }

    /**
     * Constructor.
     */
    public MaxIncreaseCountUnit(CubeNumber head, CubeNumber tail,
                                  CubeLong value, long count,
                                  long headCount, long tailCount) {
        super(head, tail, value, count);
        this.headCount = headCount;
        this.tailCount = tailCount;
    }

    private void internalMergeOp(MaxIncreaseCountUnit that) {
        CubeLong thatValue = that.value;
        value = CubeNumber.max(value, thatValue);
        long tailCount = 0L;
        if (that.headCount == thatValue.value()) {
            if (that.head.compareTo(this.tail) > 0) {
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
        this.tailCount = Math.max(tailCount, that.tailCount);
        this.count.add(that.count);
    }

    /**
     * MergeOperation.
     * @param that mergeable object
     * @return MergedUnit obj
     */
    @Override
    public MaxIncreaseCountUnit merge(MaxIncreaseCountUnit that) {
        if (that == null) {
            return this;
        }
        internalMergeOp(that);
        return this;
    }

    /**
     * fastClone.
     * @return MaxIncreaseCountUnit
     */
    @Override
    public MaxIncreaseCountUnit fastClone() {
        MaxIncreaseCountUnit maxIncreaseCountNumber = new MaxIncreaseCountUnit(
                this.head, this.tail, this.value, this.count.value(), this.headCount, this.tailCount);
        return maxIncreaseCountNumber;
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
        MaxIncreaseCountUnit maxIncreaseCountNumber = (MaxIncreaseCountUnit) other;
        if (this.headCount != maxIncreaseCountNumber.headCount) {
            return false;
        }
        if (this.tailCount != maxIncreaseCountNumber.tailCount) {
            return false;
        }
        return super.equals(other);
    }
}

