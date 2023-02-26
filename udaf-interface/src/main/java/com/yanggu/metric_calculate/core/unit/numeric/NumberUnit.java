package com.yanggu.metric_calculate.core.unit.numeric;

import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.Objects;

/**
 * 数值型
 *
 * @param <N> CubeNumber具体的实现类
 * @param <M> NumberUnit具体的实现类
 */
@Numerical
public abstract class NumberUnit<N extends CubeNumber<N>, M extends NumberUnit<N, M>>
        implements CubeNumber<M>, MergedUnit<M>, Value<Number> {

    private static final long serialVersionUID = -5364625944208766081L;

    protected CubeLong count;

    protected N value;

    /**
     * Construct.
     */
    protected NumberUnit() {
        this(null, 0);
    }

    /**
     * Construct.
     */
    protected NumberUnit(N value) {
        this(value, 1);
    }

    /**
     * Construct.
     */
    protected NumberUnit(N value, long count) {
        this(value, CubeLong.of(count));
    }

    /**
     * Construct.
     */
    protected NumberUnit(N value, CubeLong count) {
        this.value = value;
        this.count = count;
    }

    public NumberUnit setValue(N value) {
        this.value = value;
        return this;
    }

    public CubeNumber getValue() {
        return value;
    }

    public NumberUnit setCount(CubeLong count) {
        this.count = count;
        return this;
    }

    public CubeNumber getCount() {
        return this.count;
    }

    @Override
    public Number value() {
        return this.value.value();
    }

    @Override
    public M value(Number value) {
        throw new UnsupportedOperationException(getClass().getCanonicalName() + " not Support add.");
    }

    @Override
    public int intValue() {
        return (value.value() == null) ? 0 : value.intValue();
    }

    @Override
    public long longValue() {
        return (value.value() == null) ? 0L : value.longValue();
    }

    @Override
    public float floatValue() {
        return value.value().floatValue();
    }

    @Override
    public double doubleValue() {
        return (value.value() == null) ? 0.0D : value.doubleValue();
    }

    @Override
    public byte byteValue() {
        return (value.value() == null) ? 0 : value.byteValue();
    }

    @Override
    public short shortValue() {
        return (value.value() == null) ? 0 : value.shortValue();
    }

    @Override
    public M add(CubeNumber<?> num) {
        value.add(num);
        return (M) this;
    }

    @Override
    public M subtract(CubeNumber<?> num) {
        value.subtract(num);
        return (M) this;
    }

    @Override
    public M multiply(CubeNumber<?> num) {
        value.multiply(num);
        return (M) this;
    }

    @Override
    public M divide(CubeNumber<?> num) {
        value.divide(num);
        return (M) this;
    }

    /**
     * Remainder num.
     */
    @Override
    public M remainder(CubeNumber<?> num) {
        value.remainder(num);
        return (M) this;
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public M abs() {
        value.abs();
        return (M) this;
    }

    @Override
    public M negate() {
        value.negate();
        return (M) this;
    }

    @Override
    public int hashCode() {
        byte b = 31;
        int i = 1;
        i = b * i + this.count.intValue();
        return b * i + (new Double(doubleValue())).hashCode();
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
        NumberUnit otherUnit = (NumberUnit) other;
        if (!Objects.equals(this.count, otherUnit.count)) {
            return false;
        }
        return Objects.equals(value, otherUnit.value);
    }

    @Override
    public int compareTo(M that) {
        return value.compareTo(that.value);
    }

    @Override
    public String toString() {
        return String.format("%s {count=%s, value=%s}", getClass().getSimpleName(), this.count.value(), this.value);
    }

}
