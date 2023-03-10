package com.yanggu.metric_calculate.jmh;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.kryo.KryoUtil;
import com.yanggu.metric_calculate.core.kryo.pool.InputPool;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.kryo.pool.OutputPool;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class JmhTest1 {

    private static DeriveMetricCalculate<JSONObject, ?> deriveMetricCalculate1;

    private static DeriveMetricCalculate<JSONObject, ?> deriveMetricCalculate2;

    private static JSONObject input;

    private static TimedKVMetricCube<SumUnit<CubeDecimal>> metricCube;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        InputStream resourceAsStream = JmhTest1.class.getClassLoader().getResourceAsStream("test3.json");
        String jsonString = IoUtil.read(resourceAsStream).toString();
        DataDetailsWideTable dataDetailsWideTable = JSONUtil.toBean(jsonString, DataDetailsWideTable.class);
        MetricCalculate<JSONObject> metricCalculate = MetricUtil.initMetricCalculate(dataDetailsWideTable);
        deriveMetricCalculate1 = metricCalculate.getDeriveMetricCalculateList().get(0);
        deriveMetricCalculate2 = BeanUtil.copyProperties(deriveMetricCalculate1, DeriveMetricCalculate.class);
        DeriveMetricMiddleHashMapStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleStore.init();
        deriveMetricCalculate2.setDeriveMetricMiddleStore(deriveMetricMiddleStore);

        input = new JSONObject();
        input.set("account_no_out", "张三");
        input.set("trans_date", "20230308");
        input.set("debit_amt_out", 100);

        metricCube = new TimedKVMetricCube<>();
        metricCube.setKey("1_1");
        metricCube.setName("today_debit_asset");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("account_no", "张三");
        DimensionSet dimensionSet = new DimensionSet(metricCube.getKey(), metricCube.getName(), map);
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setTimeBaselineDimension(new TimeBaselineDimension(1, TimeUnit.DAY));
        metricCube.setReferenceTime(System.currentTimeMillis());
        metricCube.init();
        metricCube.put(System.currentTimeMillis(), new SumUnit<>(CubeDecimal.of("100")));

        KryoPool kryoPool = new KryoPool(true, false, 100);
        InputPool inputPool = new InputPool(true, false, 100);
        OutputPool outputPool = new OutputPool(true, false, 100);
        KryoUtil.init(kryoPool, inputPool, outputPool);
    }

    @Benchmark
    public void testUpdate_WithKryo(Blackhole blackhole) {
        List<DeriveMetricCalculateResult> deriveMetricCalculateResults = deriveMetricCalculate1.updateMetricCube(input);
        blackhole.consume(deriveMetricCalculateResults);
    }

    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        List<DeriveMetricCalculateResult> deriveMetricCalculateResults = deriveMetricCalculate2.updateMetricCube(input);
        blackhole.consume(deriveMetricCalculateResults);
    }

    @Benchmark
    public void testKryo(Blackhole blackhole) {
        byte[] serialize = KryoUtil.serialize(metricCube);
        Object deserialize = KryoUtil.deserialize(serialize);
        blackhole.consume(deserialize);
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
