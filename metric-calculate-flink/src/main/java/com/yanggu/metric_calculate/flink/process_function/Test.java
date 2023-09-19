package com.yanggu.metric_calculate.flink.process_function;

import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;


public class Test extends BroadcastProcessFunction<DeriveCalculateData, DeriveConfigData, MetricCube> {

    @Override
    public void processElement(DeriveCalculateData value,
                               BroadcastProcessFunction<DeriveCalculateData, DeriveConfigData, MetricCube>.ReadOnlyContext ctx,
                               Collector<MetricCube> out) throws Exception {

    }

    @Override
    public void processBroadcastElement(DeriveConfigData value,
                                        BroadcastProcessFunction<DeriveCalculateData, DeriveConfigData, MetricCube>.Context ctx,
                                        Collector<MetricCube> out) throws Exception {

    }

}
