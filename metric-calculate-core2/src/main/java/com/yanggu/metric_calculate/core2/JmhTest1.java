package com.yanggu.metric_calculate.core2;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.unit.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
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

    private static DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate;

    private static JSONObject input;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        InputStream resourceAsStream = JmhTest1.class.getClassLoader().getResourceAsStream("test3.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {}, true);
        MetricUtil.getFieldMap(tempMetricCalculate);
        Derive derive = tempMetricCalculate.getDerive().get(0);
        DeriveMetricCalculate<Double, Double, Double> tempderiveMetricCalculate = MetricUtil.initDerive(derive, tempMetricCalculate);

        //AggregateFieldProcessor<Double, Double, Double> aggregateFieldProcessor = new AggregateFieldProcessor<>();
        //aggregateFieldProcessor.setAggregateFunction(new SumAggregateFunction<>());
        //MetricFieldProcessor<Double> metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(tempMetricCalculate.getFieldMap(), derive.getBaseUdafParam().getMetricExpress());
        //tempderiveMetricCalculate.setMetricFieldProcessor(metricFieldProcessor);
        //tempderiveMetricCalculate.setAggregateFieldProcessor(aggregateFieldProcessor);

        DeriveMetricMiddleHashMapStore deriveMetricMiddleHashMapStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleHashMapStore.init();
        tempderiveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapStore);

        deriveMetricCalculate = tempderiveMetricCalculate;

        JSONObject tempInput = new JSONObject();
        tempInput.set("account_no_out", "000000000011");
        tempInput.set("account_no_in", "000000000012");
        tempInput.set("trans_timestamp", "1654768045000");
        tempInput.set("credit_amt_in", "100");
        tempInput.set("debit_amt_out", "800");
        tempInput.set("trans_date", "20220609");

        tempInput = MetricUtil.getParam(tempInput, MetricUtil.getFieldMap(tempMetricCalculate));

        input = tempInput;
    }

    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        List<Double> exec = deriveMetricCalculate.stateExec(input);
        blackhole.consume(exec);
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