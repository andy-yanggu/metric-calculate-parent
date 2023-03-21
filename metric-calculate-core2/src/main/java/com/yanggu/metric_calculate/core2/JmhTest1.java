package com.yanggu.metric_calculate.core2;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
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

    private static DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate1;

    private static JSONObject input;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        InputStream resourceAsStream = JmhTest1.class.getClassLoader().getResourceAsStream("test3.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {}, true);
        MetricUtil.getFieldMap(tempMetricCalculate);

        Derive derive = tempMetricCalculate.getDerive().get(0);
        deriveMetricCalculate = MetricUtil.initDerive(derive, tempMetricCalculate);
        DeriveMetricMiddleHashMapStore deriveMetricMiddleHashMapStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleHashMapStore.init();
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapStore);

        deriveMetricCalculate1 = MetricUtil.initDerive(derive, tempMetricCalculate);
        DeriveMetricMiddleHashMapKryoStore deriveMetricMiddleHashMapKryoStore = new DeriveMetricMiddleHashMapKryoStore();
        deriveMetricMiddleHashMapKryoStore.init();
        deriveMetricCalculate1.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapKryoStore);

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

    /**
     * 测试纯内存操作
     *
     * @param blackhole
     */
    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        List<DeriveMetricCalculateResult<Double>> exec = deriveMetricCalculate.stateExec(input);
        blackhole.consume(exec);
    }

    /**
     * 测试内存操作, 但是有kryo序列化和反序列化
     *
     * @param blackhole
     */
    @Benchmark
    public void testUpdate_With_Kryo(Blackhole blackhole) {
        List<DeriveMetricCalculateResult<Double>> exec = deriveMetricCalculate1.stateExec(input);
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