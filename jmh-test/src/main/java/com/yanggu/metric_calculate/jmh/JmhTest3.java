package com.yanggu.metric_calculate.jmh;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.MetricUtil;

import java.io.InputStream;
import java.util.List;

/**
 * 火焰图测试代码
 */
public class JmhTest3 {

    public static void main(String[] args) {
        InputStream resourceAsStream = JmhTest2.class.getClassLoader().getResourceAsStream("test3.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {}, true);
        MetricUtil.getFieldMap(tempMetricCalculate);

        Derive derive = tempMetricCalculate.getDerive().get(0);
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate = MetricUtil.initDerive(derive, tempMetricCalculate);
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapKryoStore();
        deriveMetricMiddleStore.init();
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);

        JSONObject tempInput = new JSONObject();
        tempInput.set("account_no_out", "000000000011");
        tempInput.set("account_no_in", "000000000012");
        tempInput.set("trans_timestamp", "1654768045000");
        tempInput.set("credit_amt_in", "100");
        tempInput.set("debit_amt_out", "800");
        tempInput.set("trans_timestamp", "1679887968782");

        tempInput = MetricUtil.getParam(tempInput, MetricUtil.getFieldMap(tempMetricCalculate));


        for (int i = 0; i < 100000000; i++) {
            List<DeriveMetricCalculateResult<Double>> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(tempInput);
        }
    }

}
