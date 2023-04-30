package com.yanggu.metric_calculate.core2.table;


import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 状态窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class StatusWindowTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    @Override
    public void put(JSONObject input) {

    }

    @Override
    public OUT query() {
        return null;
    }

}
