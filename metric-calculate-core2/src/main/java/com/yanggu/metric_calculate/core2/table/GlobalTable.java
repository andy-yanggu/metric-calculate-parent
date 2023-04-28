package com.yanggu.metric_calculate.core2.table;


import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 全窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class GlobalTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    private ACC accumulator;

    @Override
    public void put(JSONObject input) {
        accumulator = aggregateFieldProcessor.add(accumulator, getInFromInput(input));
    }

    @Override
    public OUT query() {
        return aggregateFieldProcessor.getOutFromAcc(accumulator);
    }

}
