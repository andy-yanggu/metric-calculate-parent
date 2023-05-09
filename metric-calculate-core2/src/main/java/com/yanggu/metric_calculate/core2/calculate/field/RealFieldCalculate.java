package com.yanggu.metric_calculate.core2.calculate.field;

import cn.hutool.core.convert.Convert;
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
    public String getName() {
        return columnName;
    }

    @Override
    public R process(JSONObject input) throws Exception {
        Object result = input.get(columnName);
        if (result == null) {
            return null;
        }
        if (result.getClass().equals(dataClass)) {
            return (R) result;
        } else {
            //如果字段数据类型不匹配, 进行强转
            return Convert.convert(dataClass, result);
        }
    }

}
