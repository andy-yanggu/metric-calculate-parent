package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONUtil;

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
