package com.yanggu.metric_calculate.core2.window;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

/**
 * 对无界的数据进行切分, 变成有界
 * <p>窗口是从Streaming到Batch的一个桥梁</p>
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
public abstract class AbstractWindow<IN, ACC, OUT> implements Window<OUT> {

    /**
     * 聚合字段处理器
     */
    protected AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    /**
     * 初始化接口
     */
    public void init() {
    }

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
     * 默认空实现, 对于时间类型的窗口, 需要重写该方法
     */
    @Override
    public void deleteData() {
    }

    /**
     * 默认调用query方法, 对于时间类型的窗口, 需要重写该方法
     * @param input
     * @return
     */
    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        return query();
    }

}
