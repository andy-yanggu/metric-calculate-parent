package com.yanggu.metric_calculate.core.test.jmh;


import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class JmhTest1 {

    private static MetricCalculate<JSONObject> metricCalculate;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        String jsonString = FileUtil.readUtf8String("test3.json");
        DataDetailsWideTable dataDetailsWideTable = JSONUtil.toBean(jsonString, DataDetailsWideTable.class);
        metricCalculate = MetricUtil.initMetricCalculate(dataDetailsWideTable);
    }

    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        DeriveMetricCalculate<JSONObject, ?> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(0);
        JSONObject input1 = new JSONObject();
        input1.put("account_no_out", "张三");
        input1.put("trans_date", "20230308");
        input1.put("debit_amt_out", 100);
        List<DeriveMetricCalculateResult> deriveMetricCalculateResults = deriveMetricCalculate.updateMetricCube(input1);
        blackhole.consume(deriveMetricCalculateResults);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhTest1.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

}
