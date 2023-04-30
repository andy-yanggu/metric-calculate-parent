package com.yanggu.metric_calculate.core2.table;

import cn.hutool.json.JSONObject;
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
public class SlidingCountWindowTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

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
    public OUT query() {
        return aggregateFieldProcessor.getOutFromInList(inList);
    }

}
