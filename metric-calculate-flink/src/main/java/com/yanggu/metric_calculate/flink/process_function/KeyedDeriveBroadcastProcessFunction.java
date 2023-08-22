package com.yanggu.metric_calculate.flink.process_function;


import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import com.yanggu.metric_calculate.flink.util.DeriveMetricCalculateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serializable;

import static com.yanggu.metric_calculate.flink.util.DeriveMetricCalculateUtil.deriveMapStateDescriptor;

@Slf4j
@Data
@NoArgsConstructor
public class KeyedDeriveBroadcastProcessFunction extends KeyedBroadcastProcessFunction<DimensionSet, DeriveCalculateData, DeriveConfigData, MetricCube>
                implements CheckpointedFunction, Serializable {

    private static final long serialVersionUID = 6092835299466260638L;

    private String url = "http://localhost:8888/mock-model/all-derive-data";

    @Override
    public void processElement(DeriveCalculateData value,
                               KeyedBroadcastProcessFunction<DimensionSet, DeriveCalculateData, DeriveConfigData, MetricCube>.ReadOnlyContext ctx,
                               Collector<MetricCube> out) throws Exception {
        ReadOnlyBroadcastState<Long, DeriveConfigData> broadcastState = ctx.getBroadcastState(deriveMapStateDescriptor);
        Long deriveId = value.getDeriveId();
        DeriveConfigData deriveConfigData = broadcastState.get(deriveId);
        if (deriveConfigData == null) {
            log.error("广播状态中没有派生指标配置数据: 派生指标id: {}", deriveId);
            return;
        }
        DeriveMetricCalculate deriveMetricCalculate = deriveConfigData.getDeriveMetricCalculate();
        DimensionSet dimensionSet = ctx.getCurrentKey();
        MetricCube historyMetricCube = value.getMetricCube();
        historyMetricCube = deriveMetricCalculate.addInput(value.getData(), historyMetricCube, dimensionSet);
        value.setMetricCube(historyMetricCube);
        out.collect(historyMetricCube);
    }

    @Override
    public void processBroadcastElement(DeriveConfigData value,
                                        KeyedBroadcastProcessFunction<DimensionSet, DeriveCalculateData, DeriveConfigData, MetricCube>.Context ctx,
                                        Collector<MetricCube> out) throws Exception {
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
