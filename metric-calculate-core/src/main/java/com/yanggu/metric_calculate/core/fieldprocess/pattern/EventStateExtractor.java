package com.yanggu.metric_calculate.core.fieldprocess.pattern;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;

public class EventStateExtractor implements AggregateFieldProcessor<MatchState<JSONObject>> {

    @Override
    public MatchState<JSONObject> process(JSONObject event) {
        return new MatchState<>(event);
    }

    @Override
    public String getAggregateType() {
        return null;
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return null;
    }

}