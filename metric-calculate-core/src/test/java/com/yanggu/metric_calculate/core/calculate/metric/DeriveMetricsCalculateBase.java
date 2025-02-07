package com.yanggu.metric_calculate.core.calculate.metric;


import com.alibaba.fastjson2.JSON;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.middle_store.AbstractDeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileUtil;

import java.util.List;

/**
 * 派生指标计算单元测试基类
 */
class DeriveMetricsCalculateBase {

    public static MetricCalculate metricCalculate;

    //初始化配置文件中的所有派生指标
    static {
        String jsonString = FileUtil.readUtf8String("mock_metric_config/1.json");
        MetricCalculate bean = JSON.parseObject(jsonString, MetricCalculate.class);
        // MetricCalculate bean = JSONUtil.toBean(jsonString, MetricCalculate.class);
        metricCalculate = MetricUtil.initMetricCalculate(bean);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            //设置kryoHashMap存储
            AbstractDeriveMetricMiddleStore store = new DeriveMetricMiddleHashMapKryoStore();
            store.setKryoUtil(metricCalculate.getKryoUtil());
            store.init();
            deriveMetricCalculateList.forEach(temp -> temp.setDeriveMetricMiddleStore(store));
        }
    }

}
