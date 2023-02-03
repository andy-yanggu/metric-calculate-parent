package com.yanggu.metric_calculate.core.number;

import com.yanggu.metric_calculate.core.annotation.Numerical;

import java.util.Objects;

@Numerical
public class CubeFloat implements CubeNumber<CubeFloat> {

    private Float value;

    public CubeFloat() {
    }

    public CubeFloat(Float value) {
        this.value = value;
    }

    public static CubeFloat of(Float value) {
        return new CubeFloat(value == null ? 0.0F : value);
    }

    public static CubeFloat of(Number value) {
        return new CubeFloat(value == null ? 0 : value.floatValue());
    }

    public static CubeFloat of(CubeNumber<?> cubeNumber) {
        return new CubeFloat(cubeNumber.floatValue());
    }

    /**
     * Add num.
     */
    @Override
    public CubeFloat add(CubeNumber<?> num) {
        value += num.floatValue();
        return this;
    }

    /**
     * Subtract num.
     */
    @Override
    public CubeFloat subtract(CubeNumber<?> num) {
        value -= num.floatValue();
        return this;
    }

    /**
     * Multiply num.
     */
    @Override
    public CubeFloat multiply(CubeNumber<?> num) {
        value *= num.floatValue();
        return this;
    }

    /**
     * Divide num.
     */
    @Override
    public CubeFloat divide(CubeNumber<?> num) {
        value /= num.floatValue();
        return this;
    }

    /**
     * Remainder num.
     */
    @Override
    public CubeFloat remainder(CubeNumber<?> num) {
        value %= num.floatValue();
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
    public CubeFloat abs() {
        value = (value <= 0.0F) ? 0.0F - value : value;
        return this;
    }

    /**
     * Negate.
     */
    @Override
    public CubeFloat negate() {
        value = 0.0F - value;
        return this;
    }

    @Override
    public CubeFloat fastClone() {
        return new CubeFloat(value);
    }

    @Override
    public CubeFloat value(Number value) {
        this.value = value.floatValue();
        return this;
    }

    @Override
    public Float value() {
        return value;
    }

    @Override
    public int compareTo(CubeFloat o) {
        return Float.compare(value, o.floatValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CubeFloat that = (CubeFloat) o;
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
