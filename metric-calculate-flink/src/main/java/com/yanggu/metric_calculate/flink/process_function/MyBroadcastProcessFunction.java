package com.yanggu.metric_calculate.flink.process_function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyBroadcastProcessFunction extends BroadcastProcessFunction<String, DataDetailsWideTable, Object> implements CheckpointedFunction {

    private final MapStateDescriptor<Long, MetricCalculate> metricCalculateMapStateDescriptor =
            new MapStateDescriptor<>("DataDetailsWideTable", Long.class, MetricCalculate.class);

    @Override
    public void processElement(String jsonString,
                               BroadcastProcessFunction<String, DataDetailsWideTable, Object>.ReadOnlyContext readOnlyContext,
                               Collector<Object> collector) throws Exception {
        JSONObject input = JSONUtil.parseObj(jsonString);
        Long tableId = input.getLong("tableId");
        if (tableId == null) {
            log.error("明细数据中, 没有明细宽表数据");
            return;
        }
        ReadOnlyBroadcastState<Long, MetricCalculate> broadcastState = readOnlyContext.getBroadcastState(metricCalculateMapStateDescriptor);
        MetricCalculate metricCalculate = broadcastState.get(tableId);
        if (metricCalculate == null) {
            //TODO 考虑广播流先来, 数据流没有数据的情况
            log.error("广播状态中没有数据明细宽表数据: 宽表id: {}", tableId);
            return;
        }

        input = metricCalculate.getParam(input);

        //派生指标
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
                Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(input);
                if (Boolean.FALSE.equals(filter)) {
                    continue;
                }
                JSONObject clone = input.clone();
                clone.set("deriveId", deriveMetricCalculate.getId());

                DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
                clone.set("dimensionSet", dimensionSet);

                readOnlyContext.output(new OutputTag<>("derive"), clone);
            }
        }

        //全局指标
        //List<Global> globalList = metricCalculate.getGlobal();
        //if (CollUtil.isNotEmpty(globalList)) {
        //    for (Global global : globalList) {
        //        JSONObject clone = input.clone();
        //        clone.set("globalId", global.getId());
        //        readOnlyContext.output(new OutputTag<>("global"), clone);
        //    }
        //}
    }

    @Override
    public void processBroadcastElement(DataDetailsWideTable dataDetailsWideTable,
                                        BroadcastProcessFunction<String, DataDetailsWideTable, Object>.Context context,
                                        Collector<Object> collector) throws Exception {
        BroadcastState<Long, MetricCalculate> broadcastState = context.getBroadcastState(metricCalculateMapStateDescriptor);
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(dataDetailsWideTable);
        broadcastState.put(dataDetailsWideTable.getId(), metricCalculate);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        BroadcastState<Long, MetricCalculate> broadcastState = context.getOperatorStateStore().getBroadcastState(metricCalculateMapStateDescriptor);
        Map<Long, MetricCalculate> tempMap = new HashMap<>();
        Iterator<Map.Entry<Long, MetricCalculate>> iterator = broadcastState.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, MetricCalculate> next = iterator.next();
            Long key = next.getKey();
            MetricCalculate metricCalculate = next.getValue();
            metricCalculate = MetricUtil.initMetricCalculate(metricCalculate);
            tempMap.put(key, metricCalculate);
        }
        broadcastState.clear();
        broadcastState.putAll(tempMap);
    }

}
