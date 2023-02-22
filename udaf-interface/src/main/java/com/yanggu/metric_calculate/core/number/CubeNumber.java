package com.yanggu.metric_calculate.core.number;



import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.Clone;

import java.io.Serializable;

@Numerical
public interface CubeNumber<N extends CubeNumber<N>> extends Clone<N>, Value<Number>, Comparable<N>, Serializable {

    /**
     * Add num.
     */
    N add(CubeNumber<?> num);

    /**
     * Subtract num.
     */
    N subtract(CubeNumber<?> num);

    /**
     * Multiply num.
     */
    N multiply(CubeNumber<?> num);

    /**
     * Divide num.
     */
    N divide(CubeNumber<?> num);

    /**
     * Remainder num.
     */
    N remainder(CubeNumber<?> num);

    /**
     * Set number value.
     */
    N value(Number value);

    /**
     * Signum.
     *
     * @return -1, 0, or 1 as the value of this num is negative, zero, or positive.
     */
    int signum();

    /**
     * Abs.
     */
    N abs();

    /**
     * Negate.
     */
    N negate();

    /**
     * Return int value.
     */
    default int intValue() {
        return value().intValue();
    }

    /**
     * Return long value.
     */
    default long longValue() {
        return value().longValue();
    }

    /**
     * Return float value.
     */
    default float floatValue() {
        return value().floatValue();
    }

    /**
     * Return double value.
     */
    default double doubleValue() {
        return value().doubleValue();
    }

    /**
     * Return byte value.
     */
    default byte byteValue() {
        return value().byteValue();
    }

    /**
     * Return short value.
     */
    default short shortValue() {
        return value().shortValue();
    }

    static <N extends CubeNumber<N>> N max(N n1, N n2) {
        return n1.compareTo(n2) > 0 ? n1 : n2;
    }

    static <N extends CubeNumber<N>> N min(N n1, N n2) {
        return n1.compareTo(n2) < 0 ? n1 : n2;
    }

}
