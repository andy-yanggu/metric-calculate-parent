package com.yanggu.metric_calculate.flink.process_function;


import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import com.yanggu.metric_calculate.flink.util.DeriveMetricCalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serial;
import java.io.Serializable;

import static com.yanggu.metric_calculate.flink.util.DeriveMetricCalculateUtil.deriveMapStateDescriptor;

@Slf4j
public class DeriveBroadcastProcessFunction extends BroadcastProcessFunction<MetricCube, DeriveConfigData, DeriveMetricCalculateResult>
                implements CheckpointedFunction, Serializable {

    @Serial
    private static final long serialVersionUID = -1838715009731159197L;

    private String url = "http://localhost:8888/mock-model/all-derive-data";

    @Override
    public void processElement(MetricCube value,
                               BroadcastProcessFunction<MetricCube, DeriveConfigData, DeriveMetricCalculateResult>.ReadOnlyContext ctx,
                               Collector<DeriveMetricCalculateResult> out) throws Exception {
        ReadOnlyBroadcastState<Long, DeriveConfigData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        Long deriveId = Long.parseLong(value.getDimensionSet().getKey().split("_")[1]);
        DeriveConfigData deriveConfigData = broadcastState.get(deriveId);
        if (deriveConfigData == null) {
            log.error("广播状态中没有派生指标配置数据: 派生指标id: {}", deriveId);
            return;
        }
        DeriveMetricCalculate deriveMetricCalculate = deriveConfigData.getDeriveMetricCalculate();
        deriveMetricCalculate.getWindowFactory().setWindow(value.getWindow());
        DeriveMetricCalculateResult deriveMetricCalculateResult = value.query();
        if (deriveMetricCalculateResult != null) {
            out.collect(deriveMetricCalculateResult);
        }
    }

    @Override
    public void processBroadcastElement(DeriveConfigData value,
                                        BroadcastProcessFunction<MetricCube, DeriveConfigData, DeriveMetricCalculateResult>.Context ctx,
                                        Collector<DeriveMetricCalculateResult> out) throws Exception {
        DeriveMetricCalculateUtil.initDeriveMetricCalculate(value);
        BroadcastState<Long, DeriveConfigData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        broadcastState.put(value.getDeriveMetrics().getId(), value);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        DeriveMetricCalculateUtil.initializeState(context, url);
    }

}
