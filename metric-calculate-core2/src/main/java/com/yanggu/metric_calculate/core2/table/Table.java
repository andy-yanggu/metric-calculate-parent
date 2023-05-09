package com.yanggu.metric_calculate.core2.table;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

/**
 * 对无界的数据进行切分, 变成有界
 * <p>窗口(window)就是从Streaming到Batch的一个桥梁</p>
 * <p>各实现类定义了如何将数据进行切分</p>
 * <p>从明细中提取出度量值, 并添加到状态中</p>
 * <p>定义了如何查询数据</p>
 * <p>定义了如何删除过期数据</p>
 *
 * @param <IN>
 * @param <ACC>
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
     * 查询操作
     *
     * @return
     */
    public abstract void query(JSONObject input, DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult);

    /**
     * 删除过期数据
     *
     * @param input
     */
    //public abstract void deleteExpireData(JSONObject input);

    /**
     * 从明细数据中获取度量值
     *
     * @param input
     * @return
     */
    public IN getInFromInput(JSONObject input) {
        return aggregateFieldProcessor.process(input);
    }

}
