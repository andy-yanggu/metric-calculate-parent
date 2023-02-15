package com.yanggu.metric_calculate.core.unit.mix;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Mix;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.mix_unit.MixedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Mix
@Data
@MergeType(value = "BASEMIXUNIT")
public class BaseMixedUnit implements MixedUnit<BaseMixedUnit>, Value<Map<String, Object>> {

    private Map<String, MergedUnit<?>> dataMap;

    public BaseMixedUnit() {
    }

    @Override
    public BaseMixedUnit addMergeUnit(Map<String, MergedUnit<?>> unitMap) {
        Map<String, MergedUnit<?>> thisDataMap = this.dataMap;
        if (CollUtil.isEmpty(thisDataMap)) {
            this.dataMap = unitMap;
            return this;
        }
        unitMap.forEach((thatKey, thatMergeUnit) -> {
            MergedUnit thisMergedUnit = thisDataMap.get(thatKey);
            if (thisMergedUnit == null) {
                thisDataMap.put(thatKey, thatMergeUnit);
                return;
            }
            thisMergedUnit.merge(thatMergeUnit);
            thisDataMap.put(thatKey, thisMergedUnit);
        });
        return this;
    }

    @Override
    public BaseMixedUnit merge(BaseMixedUnit that) {
        addMergeUnit(that.dataMap);
        return this;
    }

    @Override
    public BaseMixedUnit fastClone() {
        return null;
    }

    @Override
    public Map<String, Object> value() {
        Map<String, Object> returnMap = new HashMap<>();
        dataMap.forEach((k, v) -> returnMap.put(k, ValueMapper.value((Value<?>) v)));
        return returnMap;
    }

}