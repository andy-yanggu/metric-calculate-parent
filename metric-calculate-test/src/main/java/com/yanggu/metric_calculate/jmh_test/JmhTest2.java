package com.yanggu.metric_calculate.jmh_test;

import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.InputStream;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class JmhTest2 {

    private static DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate;

    private static DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate1;

    private static JSONObject input;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        InputStream resourceAsStream = JmhTest2.class.getClassLoader().getResourceAsStream("metric_config.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {});

        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tempMetricCalculate);
        DeriveMetricCalculate<Double, Double, Double> tempDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);
        deriveMetricCalculate = tempDeriveMetricCalculate;

        deriveMetricCalculate1 = BeanUtil.copyProperties(tempDeriveMetricCalculate, DeriveMetricCalculate.class);
        DeriveMetricMiddleHashMapKryoStore deriveMetricMiddleHashMapKryoStore = new DeriveMetricMiddleHashMapKryoStore();
        deriveMetricMiddleHashMapKryoStore.init();
        deriveMetricCalculate1.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapKryoStore);

        JSONObject tempInput = new JSONObject();
        tempInput.set("account_no_out", "000000000011");
        tempInput.set("account_no_in", "000000000012");
        tempInput.set("trans_timestamp", "1654768045000");
        tempInput.set("credit_amt_in", "100");
        tempInput.set("debit_amt_out", "800");
        tempInput.set("trans_timestamp", "1679887968782");

        tempInput = metricCalculate.getParam(tempInput);

        input = tempInput;
    }

    /**
     * 测试纯内存操作
     *
     * @param blackhole
     */
    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        DeriveMetricCalculateResult<Double> exec = deriveMetricCalculate.stateExec(input);
        blackhole.consume(exec);
    }

    /**
     * 测试内存操作, 但是有kryo序列化和反序列化
     *
     * @param blackhole
     */
    @Benchmark
    public void testUpdate_With_Kryo(Blackhole blackhole) {
        DeriveMetricCalculateResult<Double> exec = deriveMetricCalculate1.stateExec(input);
        blackhole.consume(exec);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhTest2.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

}