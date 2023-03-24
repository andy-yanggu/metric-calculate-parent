package com.yanggu.metric_calculate.core2.jmt_test;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.JmhTest1;
import com.yanggu.metric_calculate.core2.aggregate_function.numeric.SumAggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import com.yanggu.metric_calculate.core2.field_process.aggregate.NumberFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
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
public class NumberFieldProcessorTest {

    private static NumberFieldProcessor<Double> numberFieldProcessor;

    private static JSONObject input;

    @Setup(Level.Trial)
    public static void setup() throws Exception {
        //数值型
        NumberFieldProcessor<Double> tempNumberFieldProcessor = new NumberFieldProcessor<>();
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setMetricExpress("amount");
        tempNumberFieldProcessor.setUdafParam(baseUdafParam);
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Integer.class);
        tempNumberFieldProcessor.setFieldMap(fieldMap);
        tempNumberFieldProcessor.setNumerical(SumAggregateFunction.class.getAnnotation(Numerical.class));
        tempNumberFieldProcessor.init();
        numberFieldProcessor = tempNumberFieldProcessor;

        input = new JSONObject();
        input.set("amount", 1);
    }

    /**
     * 测试纯内存操作
     *
     * @param blackhole
     */
    @Benchmark
    public void testUpdate(Blackhole blackhole) {
        blackhole.consume(numberFieldProcessor.process(input));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(NumberFieldProcessorTest.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

}
