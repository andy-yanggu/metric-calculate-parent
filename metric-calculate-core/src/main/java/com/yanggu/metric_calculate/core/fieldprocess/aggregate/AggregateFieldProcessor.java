package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;


public interface AggregateFieldProcessor<M extends MergedUnit<M>> extends FieldProcessor<JSONObject, M> {

    String getAggregateType();

    Class<? extends MergedUnit<?>> getMergeUnitClazz();

}
