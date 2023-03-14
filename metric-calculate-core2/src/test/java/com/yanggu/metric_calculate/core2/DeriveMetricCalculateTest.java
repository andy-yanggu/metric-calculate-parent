package com.yanggu.metric_calculate.core2;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.unit.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class DeriveMetricCalculateTest {

    private MetricCalculate<JSONObject> metricCalculate;

    @Before
    public void init() {
        String jsonString = FileUtil.readUtf8String("test3.json");
        MetricCalculate<JSONObject> tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate<JSONObject>>() {}, true);
        MetricUtil.getFieldMap(tempMetricCalculate);
        this.metricCalculate = tempMetricCalculate;
    }

    @Test
    public void testExec1() {
        Derive derive = this.metricCalculate.getDerive().get(0);
        DeriveMetricCalculate<JSONObject, BigDecimal, BigDecimal, BigDecimal> deriveMetricCalculate = MetricUtil.initDerive(derive, metricCalculate);

        AggregateProcessor<JSONObject, BigDecimal, BigDecimal, BigDecimal> aggregateProcessor = new AggregateProcessor<>();
        aggregateProcessor.setAggregateFunction(new SumAggregateFunction());
        MetricFieldProcessor<JSONObject, BigDecimal> metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(this.metricCalculate.getFieldMap(), derive.getBaseUdafParam().getMetricExpress());
        aggregateProcessor.setFieldProcessor(metricFieldProcessor);
        deriveMetricCalculate.setAggregateProcessor(aggregateProcessor);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", "100");
        input.set("debit_amt_out", "800");
        input.set("trans_date", "20220609");

        deriveMetricCalculate.exec(input);
    }

}
