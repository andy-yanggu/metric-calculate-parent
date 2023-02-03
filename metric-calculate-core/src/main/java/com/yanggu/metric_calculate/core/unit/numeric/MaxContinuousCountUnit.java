package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeLong;

import java.util.Objects;

@Numerical
@MergeType("MAXCONTINUOUSCOUNT")
public class MaxContinuousCountUnit extends BoundaryUnit<Boolean, MaxContinuousCountUnit> {
    private static final long serialVersionUID = -3396993409877201343L;

    public long headCount = 0;

    public long tailCount = 0;

    public MaxContinuousCountUnit() {
    }

    /**
     * Constructor.
     */
    public MaxContinuousCountUnit(Boolean center) {
        this(center, center, CubeLong.of(center ? 1L : 0L), 1L, center ? 1L : 0L, center ? 1L : 0L);
    }

    /**
     * Constructor.
     */
    public MaxContinuousCountUnit(Boolean head,
                                    Boolean tail,
                                    CubeLong value,
                                    long count,
                                    long headCount, long tailCount) {
        super(head, tail, value, count);
        this.headCount = headCount;
        this.tailCount = tailCount;
    }

    private void internalMergeOp(MaxContinuousCountUnit that) {
        long l1 = this.value.longValue();
        long l2 = that.value.longValue();
        long l3 = Math.max(l1, l2);
        long l4 = 0L;
        if (that.headCount == l2) {
            if (that.head && this.tail) {
                l3 = Math.max(l3, that.headCount + this.tailCount);
                if (that.count.value() - that.tailCount <= 0L) {
                    l4 = that.tailCount + this.tailCount;
                }
                if (this.count.value() - this.headCount <= 0L) {
                    this.headCount += that.headCount;
                }
            }
        }
        this.value.value(l3);
        this.tail = that.tail;
        this.tailCount = Math.max(that.tailCount, l4);
        this.count.add(that.count);
    }

    @Override
    public MaxContinuousCountUnit merge(MaxContinuousCountUnit that) {
        internalMergeOp(that);
        return this;
    }

    @Override
    public MaxContinuousCountUnit fastClone() {
        return new MaxContinuousCountUnit(
                this.head, this.tail, this.value, this.count.value(), this.headCount, this.tailCount);
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
        MaxContinuousCountUnit maxContinuousCountNumber = (MaxContinuousCountUnit) other;
        if (this.headCount != maxContinuousCountNumber.headCount) {
            return false;
        }
        if (this.tailCount != maxContinuousCountNumber.tailCount) {
            return false;
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), headCount, tailCount);
    }
}

