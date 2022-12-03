package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;

@Numerical
public class DecreaseCountUnit<M extends DecreaseCountUnit<M>> extends BoundaryUnit<CubeNumber, M> {
    private static final long serialVersionUID = 1473285508708052617L;

    public DecreaseCountUnit() {
    }

    public DecreaseCountUnit(CubeNumber center) {
        super(center);
    }

    public DecreaseCountUnit(CubeNumber head, CubeNumber tail, CubeLong value, long count) {
        super(head, tail, value, count);
    }

    @Override
    public M merge(M that) {
        if (that == null) {
            return (M) this;
        }
        this.value = value
            .add(that.value)
            .add(tail.compareTo(that.head) > 0 ? CubeLong.of(1L) : CubeLong.of(0L));
        this.tail = that.tail;
        this.count.add(that.count);
        return (M) this;
    }

    @Override
    public M fastClone() {
        DecreaseCountUnit<M> decreaseCountNumber = new DecreaseCountUnit(
                this.head,
                this.tail,
                this.value,
                count.value());
        return (M) decreaseCountNumber;
    }
}

