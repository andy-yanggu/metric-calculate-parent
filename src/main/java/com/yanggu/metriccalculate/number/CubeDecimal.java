/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.number;


import com.yanggu.metriccalculate.annotation.Numerical;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

@Numerical
public class CubeDecimal implements CubeNumber<CubeDecimal> {

    private BigDecimal value;

    private int scale = 16;
    private RoundingMode mode = HALF_UP;

    public CubeDecimal() {
    }

    public CubeDecimal(BigDecimal value) {
        this.value = value;
    }

    public static CubeDecimal of(Number value) {
        return value == null ? of(BigDecimal.ZERO) : of(value.toString());
    }

    public static CubeDecimal of(String value) {
        return value == null || value.trim().isEmpty() ? of(BigDecimal.ZERO)
                : of(new BigDecimal(value));
    }

    public static CubeDecimal of(BigDecimal value) {
        return new CubeDecimal(value == null ? BigDecimal.ZERO : value);
    }

    public static CubeDecimal of(CubeNumber<?> cubeNumber) {
        return new CubeDecimal(new BigDecimal(cubeNumber.toString()));
    }

    /**
     * Add num.
     */
    @Override
    public CubeDecimal add(CubeNumber<?> num) {
        this.value = this.value.add(
            num instanceof CubeDecimal ? ((CubeDecimal) num).value : new BigDecimal(num.value().toString()));
        return this;
    }

    /**
     * Subtract num.
     */
    @Override
    public CubeDecimal subtract(CubeNumber<?> num) {
        this.value = this.value.subtract(
            num instanceof CubeDecimal ? ((CubeDecimal) num).value : new BigDecimal(num.value().toString()));
        return this;
    }

    /**
     * Multiply num.
     */
    @Override
    public CubeDecimal multiply(CubeNumber<?> num) {
        this.value = this.value.multiply(
            num instanceof CubeDecimal ? ((CubeDecimal) num).value : new BigDecimal(num.value().toString()));
        return this;
    }

    /**
     * Divide num.
     */
    @Override
    public CubeDecimal divide(CubeNumber<?> num) {
        this.value = this.value.divide(
            num instanceof CubeDecimal ? ((CubeDecimal) num).value : new BigDecimal(num.value().toString()),
            16,
            mode);
        return this;
    }

    /**
     * Remainder num.
     */
    @Override
    public CubeDecimal remainder(CubeNumber<?> num) {
        value = value.remainder(
            num instanceof CubeDecimal ? ((CubeDecimal) num).value : new BigDecimal(num.value().toString()));
        return this;
    }

    /**
     * Signum.
     *
     * @return -1, 0, or 1 as the value of this num is negative, zero, or positive.
     */
    @Override
    public int signum() {
        return value.signum();
    }

    /**
     * Abs.
     */
    @Override
    public CubeDecimal abs() {
        value = value.abs();
        return this;
    }

    /**
     * Negate.
     */
    @Override
    public CubeDecimal negate() {
        value = value.negate();
        return this;
    }

    public int scale() {
        return value.scale();
    }

    public CubeDecimal scale(int scale) {
        this.scale = scale;
        return scale(scale, HALF_UP);
    }

    /**
     * Set scale and rounding mode.
     */
    public CubeDecimal scale(int scale, RoundingMode mode) {
        this.scale = scale;
        this.mode = mode;
        value = value.setScale(scale, mode);
        return this;
    }

    public CubeDecimal scale(int scale, int mode) {
        value = value.setScale(scale, mode);
        return this;
    }

    @Override
    public CubeDecimal fastClone() {
        return new CubeDecimal(value);
    }

    @Override
    public CubeDecimal value(Number value) {
        this.value = value instanceof BigDecimal
                     ? (BigDecimal) value : new BigDecimal(value.toString()).setScale(scale, mode);
        return this;
    }

    @Override
    public BigDecimal value() {
        return value;
    }

    @Override
    public int compareTo(CubeDecimal o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CubeDecimal that = (CubeDecimal) o;
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
