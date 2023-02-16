package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;

/**
 * 聚合字段处理器
 *
 * @param <M> 生成MergeUnit
 */
public interface AggregateFieldProcessor<M extends MergedUnit<M>> extends FieldProcessor<JSONObject, M> {

    String getAggregateType();

    Class<? extends MergedUnit<?>> getMergeUnitClazz();

    /**
     * 对于复杂类型的, 可以使用回调函数
     * <p>例如对于滑动计数窗口, 使用额外的聚合字段处理器进行二次聚合</p>
     * <p>例如混合字段处理器, 对聚合后的值, 进行表达式计算</p>
     *
     * @param input
     * @return
     */
    default Object callBack(Object input) {
        return input;
    }

}
