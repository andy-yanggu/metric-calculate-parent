package com.yanggu.metric_calculate.jmh;


import cn.hutool.core.io.IoUtil;
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

import java.io.InputStream;
import java.util.List;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class JmhTest1 {

    private static MetricCalculate<JSONObject> metricCalculate;

    private static DeriveMetricCalculate<JSONObject, ?> deriveMetricCalculate;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        InputStream resourceAsStream = JmhTest1.class.getClassLoader().getResourceAsStream("test3.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        DataDetailsWideTable dataDetailsWideTable = JSONUtil.toBean(jsonString, DataDetailsWideTable.class);
        metricCalculate = MetricUtil.initMetricCalculate(dataDetailsWideTable);
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateList().get(0);
    }

    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "张三");
        input1.set("trans_date", "20230308");
        input1.set("debit_amt_out", 100);
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
