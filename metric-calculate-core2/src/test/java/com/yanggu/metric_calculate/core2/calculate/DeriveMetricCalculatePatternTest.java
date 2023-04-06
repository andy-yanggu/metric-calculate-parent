package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.table.TableFactory;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * CEP型派生指标单元测试类
 */
public class DeriveMetricCalculatePatternTest extends DeriveMetricCalculateBase {

    @Test
    public void testCEP() {
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculate(10L);

        deriveMetricCalculate.setIsCep(true);
        TableFactory<Double, Double, Double> tableFactory = new TableFactory<>();
        tableFactory.setFieldMap(metricCalculate.getFieldMap());
        tableFactory.setDeriveMetricCalculate(deriveMetricCalculate);

        Derive derive = metricCalculate.getDerive().stream()
                .filter(tempDerive -> tempDerive.getId().equals(10L))
                .findFirst().orElseThrow(() -> new RuntimeException("传入的id有误"));
        tableFactory.setDerive(derive);

        deriveMetricCalculate.setTableFactory(tableFactory);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", 100);
        input.set("debit_amt_out", 800);
        input.set("trans_date", "20220609");
        List<DeriveMetricCalculateResult<Double>> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(input);
        System.out.println(deriveMetricCalculateResults);
    }

}
