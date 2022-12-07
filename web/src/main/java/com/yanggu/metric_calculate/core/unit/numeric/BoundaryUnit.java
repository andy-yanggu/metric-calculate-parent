package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.number.CubeLong;

public abstract class BoundaryUnit<T, M extends NumberUnit<CubeLong, M>> extends NumberUnit<CubeLong, M> {
    private static final long serialVersionUID = -2556274529298273739L;

    public T head;

    public T tail;

    public BoundaryUnit() {
    }

    public BoundaryUnit(T center) {
        this(center, center, CubeLong.of(0L), 1L);
    }

    /**
     * Construct.
     */
    public BoundaryUnit(T head, T tail, CubeLong value, long count) {
        super(value, count);
        setHead(head);
        setTail(tail);
    }

    public T getHead() {
        return this.head;
    }

    public void setHead(T head) {
        this.head = head;
    }

    public T getTail() {
        return this.tail;
    }

    public void setTail(T tail) {
        this.tail = tail;
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
        BoundaryUnit<T, M> boundaryUnit = (BoundaryUnit) other;
        if (this.head == null) {
            if (boundaryUnit.head != null) {
                return false;
            }
        } else if (!this.head.equals(boundaryUnit.head)) {
            return false;
        }
        if (this.tail == null) {
            if (boundaryUnit.tail != null) {
                return false;
            }
        } else if (!this.tail.equals(boundaryUnit.tail)) {
            return false;
        }
        return super.equals(other);
    }
}
