package com.yanggu.metric_calculate.core2.field_process.aggregate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import lombok.SneakyThrows;

import java.util.Collection;

/**
 * 聚合字段处理器
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public class AggregateFieldProcessor<IN, ACC, OUT> {

    /**
     * 从输入的明细数据中提取出度量值
     */
    private final FieldProcessor<JSONObject, IN> fieldProcessor;

    /**
     * 聚合函数
     */
    private final AggregateFunction<IN, ACC, OUT> aggregateFunction;

    public AggregateFieldProcessor(FieldProcessor<JSONObject, IN> fieldProcessor,
                                   AggregateFunction<IN, ACC, OUT> aggregateFunction) {
        this.fieldProcessor = fieldProcessor;
        this.aggregateFunction = aggregateFunction;
    }

    /**
     * 从明细数据中提取出度量值
     *
     * @param input 明细数据
     * @return 度量值
     */
    @SneakyThrows
    public IN process(JSONObject input) {
        return fieldProcessor.process(input);
    }

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
