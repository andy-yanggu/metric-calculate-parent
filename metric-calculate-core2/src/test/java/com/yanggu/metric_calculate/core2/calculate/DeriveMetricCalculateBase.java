package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.util.MetricUtil;

/**
 * 派生指标计算单元测试基类
 */
public class DeriveMetricCalculateBase {

    public static MetricCalculate metricCalculate;

    //初始化配置文件中的所有派生指标
    static {
        String jsonString = FileUtil.readUtf8String("metric_config.json");
        metricCalculate = MetricUtil.initMetricCalculate(JSONUtil.toBean(jsonString, MetricCalculate.class));
    }

}
