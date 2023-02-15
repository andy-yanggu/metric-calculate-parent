package com.yanggu.metric_calculate.core.unit.mix_unit;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.List;


public class MixUnitImpl implements MixedUnit<MixUnitImpl>, Value<List<MergedUnit<?>>> {

    private List<MergedUnit<?>> mergedUnitList;

    public MixUnitImpl() {
    }

    @Override
    public MixUnitImpl addMergeUnit(List<MergedUnit<?>> list) {
        mergedUnitList = list;
        return this;
    }

    @Override
    public MixUnitImpl merge(MixUnitImpl that) {

        List<MergedUnit<?>> thisMergedUnitList = mergedUnitList;
        List<MergedUnit<?>> thatMergedUnitList = that.mergedUnitList;
        int size = thisMergedUnitList.size();
        for (int i = 0; i < size; i++) {
            MergedUnit thisUnit = thisMergedUnitList.get(i);
            MergedUnit<?> thatUnit = thatMergedUnitList.get(i);
            thisUnit.merge(thatUnit);
        }
        return this;
    }

    @Override
    public MixUnitImpl fastClone() {
        MixUnitImpl mixUnit = new MixUnitImpl();
        mixUnit.mergedUnitList = mergedUnitList;
        return mixUnit;
    }

    @Override
    public List<MergedUnit<?>> value() {
        return mergedUnitList;
    }

}
