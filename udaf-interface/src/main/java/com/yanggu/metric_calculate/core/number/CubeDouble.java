package com.yanggu.metric_calculate.core.number;


import com.yanggu.metric_calculate.core.annotation.Numerical;

import java.util.Objects;

@Numerical
public class CubeDouble implements CubeNumber<CubeDouble> {

    private Double value;

    public CubeDouble() {
    }

    public CubeDouble(Double value) {
        this.value = value;
    }

    public static CubeDouble of(Double value) {
        return new CubeDouble(value == null ? 0.0 : value);
    }

    public static CubeDouble of(Number value) {
        return new CubeDouble(value == null ? 0 : value.doubleValue());
    }

    public static CubeDouble of(CubeNumber<?> cubeNumber) {
        return new CubeDouble(cubeNumber.doubleValue());
    }

    /**
     * Add num.
     */
    @Override
    public CubeDouble add(CubeNumber<?> num) {
        value += num.doubleValue();
        return this;
    }

    /**
     * Subtract num.
     */
    @Override
    public CubeDouble subtract(CubeNumber<?> num) {
        value -= num.doubleValue();
        return this;
    }

    /**
     * Multiply num.
     */
    @Override
    public CubeDouble multiply(CubeNumber<?> num) {
        value *= num.doubleValue();
        return this;
    }

    /**
     * Divide num.
     */
    @Override
    public CubeDouble divide(CubeNumber<?> num) {
        value /= num.doubleValue();
        return this;
    }

    /**
     * Remainder num.
     */
    @Override
    public CubeDouble remainder(CubeNumber<?> num) {
        value %= num.doubleValue();
        return this;
    }

    /**
     * Signum.
     *
     * @return -1, 0, or 1 as the value of this num is negative, zero, or positive.
     */
    @Override
    public int signum() {
        return value > 0 ? 1 : value == 0 ? 0 : -1;
    }

    /**
     * Abs.
     */
    @Override
    public CubeDouble abs() {
        value = (value <= 0.0D) ? 0.0D - value : value;
        return this;
    }

    /**
     * Negate.
     */
    @Override
    public CubeDouble negate() {
        value = 0.0D - value;
        return this;
    }

    @Override
    public CubeDouble fastClone() {
        return new CubeDouble(value);
    }

    @Override
    public CubeDouble value(Number value) {
        this.value = value.doubleValue();
        return this;
    }

    @Override
    public Double value() {
        return value;
    }

    @Override
    public int compareTo(CubeDouble o) {
        return Double.compare(value, o.doubleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CubeDouble that = (CubeDouble) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
