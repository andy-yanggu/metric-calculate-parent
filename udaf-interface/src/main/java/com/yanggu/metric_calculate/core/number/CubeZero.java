package com.yanggu.metric_calculate.core.number;


import com.yanggu.metric_calculate.core.annotation.Numerical;

@Numerical
public class CubeZero<N extends CubeNumber<N>> implements CubeNumber<N> {
    /**
     * Add num.
     */
    @Override
    public N add(CubeNumber<?> num) {
        return (N) num.fastClone();
    }

    /**
     * Subtract num.
     */
    @Override
    public N subtract(CubeNumber<?> num) {
        return (N) num.fastClone().negate();
    }

    /**
     * Multiply num.
     */
    @Override
    public N multiply(CubeNumber<?> num) {
        return (N) this;
    }

    /**
     * Divide num.
     */
    @Override
    public N divide(CubeNumber<?> num) {
        return (N) this;
    }

    /**
     * Remainder num.
     */
    @Override
    public N remainder(CubeNumber<?> num) {
        return (N) this;
    }

    /**
     * Set number value.
     */
    @Override
    public N value(Number value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Number value() {
        return 0;
    }

    /**
     * Signum.
     *
     * @return -1, 0, or 1 as the value of this num is negative, zero, or positive.
     */
    @Override
    public int signum() {
        return 0;
    }

    /**
     * Abs.
     */
    @Override
    public N abs() {
        return (N) this;
    }

    /**
     * Negate.
     */
    @Override
    public N negate() {
        return (N) this;
    }

    @Override
    public N fastClone() {
        return (N) new CubeZero();
    }

    @Override
    public int compareTo(CubeNumber o) {
        return o.signum();
    }

    @Override
    public String toString() {
        return "0";
    }
}
