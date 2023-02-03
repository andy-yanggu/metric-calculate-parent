package com.yanggu.metric_calculate.core.number;

import com.yanggu.metric_calculate.core.annotation.Numerical;

import java.util.Objects;

@Numerical
public class CubeInteger implements CubeNumber<CubeInteger> {

    private Integer value;

    public CubeInteger() {
    }

    public CubeInteger(Integer value) {
        this.value = value;
    }

    public static CubeInteger of(Integer value) {
        return new CubeInteger(value == null ? 0 : value);
    }

    public static CubeInteger of(Number value) {
        return new CubeInteger(value == null ? 0 : value.intValue());
    }

    public static CubeInteger of(CubeNumber<?> cubeNumber) {
        return new CubeInteger(cubeNumber.intValue());
    }

    /**
     * Set number value.
     */
    @Override
    public CubeInteger value(Number value) {
        this.value = value.intValue();
        return this;
    }

    @Override
    public Integer value() {
        return value;
    }

    /**
     * Add num.
     */
    @Override
    public CubeInteger add(CubeNumber<?> num) {
        value += num.intValue();
        return this;
    }

    /**
     * Subtract num.
     */
    @Override
    public CubeInteger subtract(CubeNumber<?> num) {
        value -= num.intValue();
        return this;
    }

    /**
     * Multiply num.
     */
    @Override
    public CubeInteger multiply(CubeNumber<?> num) {
        value *= num.intValue();
        return this;
    }

    /**
     * Divide num.
     */
    @Override
    public CubeInteger divide(CubeNumber<?> num) {
        value /= num.intValue();
        return this;
    }

    /**
     * Remainder num.
     */
    @Override
    public CubeInteger remainder(CubeNumber<?> num) {
        value %= num.intValue();
        return this;
    }

    /**
     * Signum.
     *
     * @return -1, 0, or 1 as the value of this num is negative, zero, or positive.
     */
    @Override
    public int signum() {
        if (value > 0) {
            return 1;
        } else {
            if (value == 0) {
                return 0;
            }
            return -1;
        }
    }

    /**
     * Abs.
     */
    @Override
    public CubeInteger abs() {
        value = (value < 0) ? -value : value;
        return this;
    }

    /**
     * Negate.
     */
    @Override
    public CubeInteger negate() {
        value = -value;
        return this;
    }

    @Override
    public CubeInteger fastClone() {
        return new CubeInteger(value);
    }

    @Override
    public int compareTo(CubeInteger that) {
        return Integer.compare(value, that.intValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CubeInteger that = (CubeInteger) o;
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
