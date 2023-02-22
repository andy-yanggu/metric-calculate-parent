package com.yanggu.metric_calculate.core.test_unit;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;

import java.util.Map;

/**
 * 数值型, 使用自定义参数
 * <p>限定求和不能超过给定的值</p>
 * <p>通过params进行传递</p>
 */
@MergeType(value = "SUM2", useParam = true)
@Numerical
public class SumUnit extends NumberUnit<CubeDecimal, SumUnit> {

    private Double maxValue;

    public SumUnit() {
        super();
    }

    public SumUnit(CubeNumber value, Map<String, Object> params) {
        this(CubeDecimal.of(value));
        this.maxValue = Double.parseDouble(params.get("maxValue").toString());
    }

    public SumUnit(CubeDecimal value) {
        super(value, 1L);
    }

    public SumUnit(CubeDecimal value, long count) {
        super(value, count);
    }

    @Override
    public Number value() {
        return (this.value == null) ? 0.0D : this.value.doubleValue();
    }

    @Override
    public SumUnit merge(SumUnit that) {
        if (that == null) {
            return this;
        }
        double result = value.fastClone().add(that.value).doubleValue();
        if (result > maxValue) {
            return this;
        }
        value.add(that.value);
        count.add(that.count);
        return this;
    }

    @Override
    public SumUnit fastClone() {
        return new SumUnit(value.fastClone(), count.longValue());
    }

}

