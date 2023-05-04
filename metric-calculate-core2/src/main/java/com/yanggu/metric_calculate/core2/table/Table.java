package com.yanggu.metric_calculate.core2.table;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

/**
 * 对数据进行切分
 *
 * @param <IN>
 * @param <OUT>
 */
@Data
public abstract class Table<IN, ACC, OUT> {

    protected AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    /**
     * 初始化接口
     */
    public void init() {
    }

    /**
     * 放入明细数据进行累加
     *
     * @param input
     */
    public abstract void put(JSONObject input);

    /**
     * 从明细数据中获取度量值
     *
     * @param input
     * @return
     */
    public IN getInFromInput(JSONObject input) {
        return aggregateFieldProcessor.process(input);
    }

    /**
     * 查询操作
     *
     * @return
     */
    public abstract void query(JSONObject input, DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult);

}
