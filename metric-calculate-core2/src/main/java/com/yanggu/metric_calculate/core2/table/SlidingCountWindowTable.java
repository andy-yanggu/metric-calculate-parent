package com.yanggu.metric_calculate.core2.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动计数窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class SlidingCountWindowTable<IN, ACC, OUT> extends AbstractTable<IN, ACC, OUT, SlidingCountWindowTable<IN, ACC, OUT>> {

    private Integer limit;

    private List<IN> inList = new ArrayList<>();

    @Override
    public void put(JSONObject input) {
        IN in = aggregateFieldProcessor.process(input);
        inList.add(in);
        while (inList.size() > limit) {
            inList.remove(0);
        }
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        return query(null);
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        OUT outFromInList = aggregateFieldProcessor.getOutFromInList(inList);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(outFromInList);
        return deriveMetricCalculateResult;
    }

    @Override
    public SlidingCountWindowTable<IN, ACC, OUT> merge(SlidingCountWindowTable<IN, ACC, OUT> thatTable) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(inList);
    }

}
