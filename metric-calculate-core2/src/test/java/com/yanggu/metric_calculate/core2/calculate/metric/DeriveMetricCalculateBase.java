package com.yanggu.metric_calculate.core2.calculate.metric;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.util.MetricUtil;

import java.util.List;

/**
 * 派生指标计算单元测试基类
 */
class DeriveMetricCalculateBase {

    public static MetricCalculate metricCalculate;

    //初始化配置文件中的所有派生指标
    static {
        String jsonString = FileUtil.readUtf8String("metric_config.json");
        metricCalculate = MetricUtil.initMetricCalculate(JSONUtil.toBean(jsonString, MetricCalculate.class));
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            //设置kryoHashMap存储
            DeriveMetricMiddleStore store = new DeriveMetricMiddleHashMapKryoStore();
            store.init();
            deriveMetricCalculateList.forEach(temp -> temp.setDeriveMetricMiddleStore(store));
        }
    }

}
