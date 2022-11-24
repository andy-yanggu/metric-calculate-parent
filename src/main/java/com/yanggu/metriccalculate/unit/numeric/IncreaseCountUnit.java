/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.numeric;

import com.yanggu.metriccalculate.annotation.Numerical;
import com.yanggu.metriccalculate.number.CubeLong;
import com.yanggu.metriccalculate.number.CubeNumber;

@Numerical
public class IncreaseCountUnit<M extends IncreaseCountUnit<M>> extends BoundaryUnit<CubeNumber, M> {
    private static final long serialVersionUID = 822979245308734777L;

    public IncreaseCountUnit() {
    }

    public IncreaseCountUnit(CubeNumber center) {
        super(center);
    }

    public IncreaseCountUnit(CubeNumber head, CubeNumber tail, CubeLong value, long count) {
        super(head, tail, value, count);
    }

    @Override
    public M merge(M that) {
        if (that == null) {
            return (M) this;
        }
        this.value = this.value
            .add(that.value)
            .add(tail.compareTo(that.head) < 0 ? CubeLong.of(1L) : CubeLong.of(0L));
        this.tail = that.tail;
        count.add(that.count);
        return (M) this;
    }

    @Override
    public M fastClone() {
        IncreaseCountUnit increaseCountNumber = new IncreaseCountUnit(
                    this.head, this.tail, this.value, this.count.value());
        return (M) increaseCountNumber;
    }
}

