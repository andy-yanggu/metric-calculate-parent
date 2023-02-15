package com.yanggu.metric_calculate.core.unit.mix;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.mix_unit.MixedUnit;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.List;
import java.util.Map;

@MergeType(value = "BASEMIXUNIT")
public class BaseMixedUnit implements MixedUnit<BaseMixedUnit>, Value<Map<String, MergedUnit<?>>> {

    private Map<String, MergedUnit<?>> dataMap;

    public BaseMixedUnit() {
    }

    @Override
    public BaseMixedUnit merge(BaseMixedUnit that) {
        return null;
    }

    @Override
    public BaseMixedUnit fastClone() {
        return null;
    }

    @Override
    public BaseMixedUnit addMergeUnit(List<MergedUnit<?>> list) {
        return null;
    }

    @Override
    public Map<String, MergedUnit<?>> value() {
        return null;
    }
}