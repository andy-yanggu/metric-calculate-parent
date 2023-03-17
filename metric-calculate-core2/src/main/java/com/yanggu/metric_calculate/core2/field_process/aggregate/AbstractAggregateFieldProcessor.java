package com.yanggu.metric_calculate.core2.field_process.aggregate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;

import java.util.Collection;

public abstract class AbstractAggregateFieldProcessor<IN, ACC, OUT> implements FieldProcessor<JSONObject, IN> {
    
    private final AggregateFunction<IN, ACC, OUT> aggregateFunction;

    protected AbstractAggregateFieldProcessor(AggregateFunction<IN, ACC, OUT> aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    @Override
    public void init() throws Exception {
        aggregateFunction.init();
    }

    /**
     * 从明细数据中提取出度量值
     *
     * @param input
     * @return
     * @throws Exception
     */
    public abstract IN process(JSONObject input);

    /**
     * 将度量值添加到累加器中, 并返回累加器
     *
     * @param oldAcc
     * @param in
     * @return
     */
    public ACC add(ACC oldAcc, IN in) {
        if (oldAcc == null) {
            oldAcc = aggregateFunction.createAccumulator();
        }
        oldAcc = aggregateFunction.add(in, oldAcc);
        return oldAcc;
    }

    /**
     * 从累加器中获取输出值
     *
     * @param accumulator
     * @return
     */
    public OUT getOut(ACC accumulator) {
        return aggregateFunction.getResult(accumulator);
    }

    /**
     * 合并多个累加器并输出
     *
     * @param accList
     * @return
     */
    public OUT getMergeResult(Collection<ACC> accList) {
        ACC accumulator = aggregateFunction.createAccumulator();
        accumulator = accList.stream().reduce(accumulator, aggregateFunction::merge);
        return aggregateFunction.getResult(accumulator);
    }

}
