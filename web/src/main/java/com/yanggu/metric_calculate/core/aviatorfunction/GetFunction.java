package com.yanggu.metric_calculate.core.aviatorfunction;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import lombok.SneakyThrows;

import java.util.Map;

import static com.yanggu.metric_calculate.core.constant.Constant.DERIVE_METRIC_META_DATA;
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

        //指标的类型(派生、全局)
        //Map<String, String> metricRefer = (Map<String, String>) env.get("METRIC_REFER");
        //String metricType = metricRefer.get(metricName);

        //获取指标元数据信息
        //衍生指标
        Map<String, DeriveMetricCalculate> metricMetaDataMap =
                (Map<String, DeriveMetricCalculate>) env.get(DERIVE_METRIC_META_DATA);
        if (CollUtil.isEmpty(metricMetaDataMap)) {
            throw new RuntimeException("衍生指标map为空");
        }

        DeriveMetricCalculate derive = metricMetaDataMap.get(metricName);
        if (derive == null) {
            throw new RuntimeException("没有对应的衍生指标" + metricName);
        }

        //dingo客户端
        //DingoClient dingoClient = (DingoClient) env.get(DINGO_CLIENT);
        //if (dingoClient == null) {
        //    throw new RuntimeException("Dingo客户端为空");
        //}
        //
        ////取第一个指标存储宽表
        //StoreTable storeTable = CollUtil.getFirst(derive.getStore().getStoreTableList());
        //
        ////构建出dingo的主键
        //Key key = derive.buildKey(originData, day);
        //Record recordData = dingoClient.get(key);
        //
        ////如果为空, 返回nil
        //if (recordData == null) {
        //    return AviatorNil.NIL;
        //}
        //Object value = recordData.getValue(storeTable.getStoreColumn());
        //return AviatorRuntimeJavaType.valueOf(value);
        return AviatorNil.NIL;
    }

}
