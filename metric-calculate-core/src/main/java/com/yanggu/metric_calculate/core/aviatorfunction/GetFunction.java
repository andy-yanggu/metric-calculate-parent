package com.yanggu.metric_calculate.core.aviatorfunction;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import lombok.SneakyThrows;

import java.util.Map;

import static com.yanggu.metric_calculate.core.constant.Constant.ORIGIN_DATA;

/**
 * 自定义get函数.
 * 通过指标名和时间取数据
 * get(指标名, -1)
 */
public class GetFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "get";
    }

    @SneakyThrows
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        //指标名
        String metricName = ((AviatorJavaType) arg1).getName();
        //查询天数
        int day = FunctionUtils.getNumberValue(arg2, env).intValue();

        //原始数据
        JSONObject originData = (JSONObject) env.get(ORIGIN_DATA);
        if (CollUtil.isEmpty((Map<?, ?>) originData)) {
            throw new RuntimeException("原始数据为空");
        }

        return AviatorNil.NIL;
    }

}
