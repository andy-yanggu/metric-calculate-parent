package com.yanggu.metric_calculate.jmh_test;

import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.kryo.KryoUtil;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.json.JSONUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class JmhTest2 {

    private static DeriveMetricCalculate<Double, Double, Double> memoryDeriveMetricCalculate;

    private static DeriveMetricCalculate<Double, Double, Double> kryoDeriveMetricCalculate;

    private static Map<String, Object> input;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        String jsonString = ResourceUtil.readUtf8Str("mock_metric_config/1.json");
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, MetricCalculate.class);

        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tempMetricCalculate);
        DeriveMetricCalculate<Double, Double, Double> tempDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);
        memoryDeriveMetricCalculate = tempDeriveMetricCalculate;

        kryoDeriveMetricCalculate = new DeriveMetricCalculate<>();
        kryoDeriveMetricCalculate = BeanUtil.copyProperties(tempDeriveMetricCalculate, kryoDeriveMetricCalculate);
        DeriveMetricMiddleHashMapKryoStore deriveMetricMiddleHashMapKryoStore = new DeriveMetricMiddleHashMapKryoStore();
        deriveMetricMiddleHashMapKryoStore.setKryoUtil(KryoUtil.INSTANCE);
        deriveMetricMiddleHashMapKryoStore.init();
        kryoDeriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapKryoStore);

        Map<String, Object> tempInput = new HashMap<>();
        tempInput.put("account_no_out", "000000000011");
        tempInput.put("account_no_in", "000000000012");
        tempInput.put("amount", 800.0D);
        tempInput.put("trans_timestamp", 1679887968782L);

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
        DeriveMetricCalculateResult<Double> exec = memoryDeriveMetricCalculate.stateExec(input);
        blackhole.consume(exec);
    }

    /**
     * 测试内存操作, 但是有kryo序列化和反序列化
     *
     * @param blackhole
     */
    @Benchmark
    public void testUpdate_With_Kryo(Blackhole blackhole) {
        DeriveMetricCalculateResult<Double> exec = kryoDeriveMetricCalculate.stateExec(input);
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