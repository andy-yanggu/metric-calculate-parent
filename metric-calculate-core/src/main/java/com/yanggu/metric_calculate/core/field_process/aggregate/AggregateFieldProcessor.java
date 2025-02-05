package com.yanggu.metric_calculate.core.field_process.aggregate;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.Map;

/**
 * 聚合字段处理器
 * <p>主要有度量字段处理器和聚合函数</p>
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 * @param fieldProcessor    从输入的明细数据中提取出度量值
 * @param aggregateFunction 聚合函数
 */
public record AggregateFieldProcessor<IN, ACC, OUT>(FieldProcessor<Map<String, Object>, IN> fieldProcessor,
                                                    AggregateFunction<IN, ACC, OUT> aggregateFunction) {

    /**
     * 从明细数据中提取出度量值
     *
     * @param input 明细数据
     * @return 度量值
     */
    @SneakyThrows
    public IN process(Map<String, Object> input) {
        return fieldProcessor.process(input);
    }

    /**
     * 创建累加器
     *
     * @return
     */
    public ACC createAcc() {
        return aggregateFunction.createAccumulator();
    }

    /**
     * 将度量值添加到累加器中, 并返回累加器
     *
     * @param oldAcc 历史中间状态数据
     * @param in     度量值
     * @return 新的累加器
     */
    public ACC add(ACC oldAcc, IN in) {
        if (oldAcc == null) {
            oldAcc = createAcc();
        }
        return aggregateFunction.add(in, oldAcc);
    }

    /**
     * 从累加器中获取输出值
     *
     * @param accumulator 累加器
     * @return
     */
    public OUT getOutFromAcc(ACC accumulator) {
        if (accumulator == null) {
            return null;
        }
        return aggregateFunction.getResult(accumulator);
    }

    /**
     * 合并多个累加器
     *
     * @param accList
     * @return
     */
    public ACC mergeAccList(List<ACC> accList) {
        if (CollUtil.isEmpty(accList)) {
            return null;
        }
        ACC accumulator = createAcc();
        for (ACC acc : accList) {
            accumulator = aggregateFunction.merge(accumulator, acc);
        }
        return accumulator;
    }

    /**
     * 合并多个累加器并输出
     *
     * @param accList
     * @return
     */
    public OUT getMergeResult(List<ACC> accList) {
        if (CollUtil.isEmpty(accList)) {
            return null;
        }
        ACC accumulator = mergeAccList(accList);
        return getOutFromAcc(accumulator);
    }

    /**
     * 输入明细, 返回聚合值
     *
     * @param inList
     * @return
     */
    public OUT getOutFromInList(List<IN> inList) {
        ACC acc = createAcc();
        for (IN in : inList) {
            acc = aggregateFunction.add(in, acc);
        }
        return getOutFromAcc(acc);
    }

}
