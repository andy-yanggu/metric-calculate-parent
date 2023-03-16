package com.yanggu.metric_calculate.core2.field_process.aggregate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.Collection;

public interface AggregateFieldProcessor<IN, ACC, OUT> extends FieldProcessor<JSONObject, IN> {

    /**
     * 从明细数据中提取出度量值
     *
     * @param input
     * @return
     * @throws Exception
     */
    IN process(JSONObject input);

    AggregateFunction<IN, ACC, OUT> getAggregateFunction();

    /**
     * 将度量值添加到累加器中
     *
     * @param oldAcc
     * @param in
     * @return
     */
    default ACC add(ACC oldAcc, IN in) {
        if (oldAcc == null) {
            oldAcc = getAggregateFunction().createAccumulator();
        }
        oldAcc = getAggregateFunction().add(in, oldAcc);
        return oldAcc;
    }

    default OUT getOut(ACC accumulator) {
        return getAggregateFunction().getResult(accumulator);
    }

    default OUT getMergeResult(Collection<ACC> accList) {
        ACC accumulator = getAggregateFunction().createAccumulator();
        accumulator = accList.stream().reduce(accumulator, (acc, acc2) -> getAggregateFunction().merge(acc, acc2));
        return getAggregateFunction().getResult(accumulator);
    }

}
