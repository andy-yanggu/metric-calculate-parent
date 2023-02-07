package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;

public class EventStateExtractor extends BaseAggregateFieldProcessor<MatchState<JSONObject>> {

    @Override
    public void init() throws Exception {
    }

    @Override
    public MatchState<JSONObject> process(JSONObject event) {
        return new MatchState<>(event);
    }

}