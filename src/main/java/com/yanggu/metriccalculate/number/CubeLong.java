/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.number;

import com.yanggu.metriccalculate.annotation.Numerical;

import java.util.Objects;

@Numerical
public class CubeLong implements CubeNumber<CubeLong> {

    private Long value;

    public CubeLong() {
    }

    public CubeLong(Long value) {
        this.value = value;
    }

    public static CubeLong of(Long value) {
        return new CubeLong(value == null ? 0 : value);
    }

    public static CubeLong of(Number value) {
        return new CubeLong(value == null ? 0 : value.longValue());
    }

    public static CubeLong of(CubeNumber<?> cubeNumber) {
        return new CubeLong(cubeNumber.longValue());
    }

    /**
     * Set number value.
     */
    @Override
    public CubeLong value(Number value) {
        this.value = value.longValue();
        return this;
    }

    @Override
    public Long value() {
        return value;
    }

    public CubeLong inc() {
        value++;
        return this;
    }

    public CubeLong inc(Long inc) {
        value += inc;
        return this;
    }

    /**
     * Add num.
     */
    @Override
    public CubeLong add(CubeNumber<?> num) {
        value += num.longValue();
        return this;
    }

    /**
     * Subtract num.
     */
    @Override
    public CubeLong subtract(CubeNumber<?> num) {
        value -= num.longValue();
        return this;
    }

    /**
     * Multiply num.
     */
    @Override
    public CubeLong multiply(CubeNumber<?> num) {
        value *= num.longValue();
        return this;
    }

    /**
     * Divide num.
     */
    @Override
    public CubeLong divide(CubeNumber<?> num) {
        value /= num.longValue();
        return this;
    }

    /**
     * Remainder num.
     */
    @Override
    public CubeLong remainder(CubeNumber<?> num) {
        value %= num.longValue();
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
    public CubeLong abs() {
        value = (value < 0) ? -value : value;
        return this;
    }

    /**
     * Negate.
     */
    @Override
    public CubeLong negate() {
        value = -value;
        return this;
    }

    @Override
    public CubeLong fastClone() {
        return new CubeLong(value);
    }

    @Override
    public int compareTo(CubeLong that) {
        return Long.compare(value, that.longValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CubeLong that = (CubeLong) o;
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
