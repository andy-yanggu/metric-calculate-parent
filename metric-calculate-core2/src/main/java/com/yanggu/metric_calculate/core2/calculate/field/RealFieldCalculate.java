package com.yanggu.metric_calculate.core2.calculate.field;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * 真实字段处理器
 * <p>传入的JSON数据中包含该字段, 只是可能字段类型不一致, 需要处理</p>
 *
 * @param <R>
 */
@Data
public class RealFieldCalculate<R> implements FieldCalculate<JSONObject, R> {

    private String columnName;

    private Class<R> dataClass;

    @Override
    public void init() {
        if (StrUtil.isBlank(columnName)) {
            throw new RuntimeException("字段名为空");
        }

        if (dataClass == null) {
            throw new RuntimeException("字段数据类型为空");
        }
    }

    @Override
    public String getName() {
        return columnName;
    }

    @Override
    public R process(JSONObject input) throws Exception {
        if (CollUtil.isEmpty((Map<?, ?>) input)) {
            throw new RuntimeException("传入的数据为空");
        }
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
