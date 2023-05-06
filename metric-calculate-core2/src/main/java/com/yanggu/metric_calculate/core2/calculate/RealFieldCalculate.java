package com.yanggu.metric_calculate.core2.calculate;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 真实字段处理器
 *
 * @param <R>
 */
@Data
public class RealFieldCalculate<R> implements FieldCalculate<JSONObject, R> {

    private String columnName;

    private Class<R> dataClass;

    @Override
    public R process(JSONObject input) throws Exception {
        return input.get(columnName, dataClass);
    }

}
