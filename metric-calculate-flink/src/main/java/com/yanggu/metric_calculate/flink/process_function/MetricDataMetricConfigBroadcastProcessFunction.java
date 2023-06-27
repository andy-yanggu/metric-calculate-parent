package com.yanggu.metric_calculate.flink.process_function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.flink.pojo.DeriveCalculateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.flink.util.Constant.*;

@Slf4j
public class MetricDataMetricConfigBroadcastProcessFunction extends BroadcastProcessFunction<String, DataDetailsWideTable, Void>
        implements CheckpointedFunction, Serializable {

    private static final long serialVersionUID = 7881762197491832138L;

    private final MapStateDescriptor<Long, MetricCalculate> mapStateDescriptor =
            new MapStateDescriptor<>("DataDetailsWideTable", Long.class, MetricCalculate.class);

    private String url = "http://localhost:8888/mock-model/all-data";

    @Override
    public void processElement(String jsonString,
                               BroadcastProcessFunction<String, DataDetailsWideTable, Void>.ReadOnlyContext readOnlyContext,
                               Collector<Void> collector) throws Exception {
        JSONObject input = JSONUtil.parseObj(jsonString);
        Long tableId = input.getLong("tableId");
        if (tableId == null) {
            log.error("明细数据中, 没有明细宽表数据");
            return;
        }
        ReadOnlyBroadcastState<Long, MetricCalculate> broadcastState = readOnlyContext.getBroadcastState(mapStateDescriptor);
        MetricCalculate metricCalculate = broadcastState.get(tableId);
        if (metricCalculate == null) {
            log.error("广播状态中没有数据明细宽表数据: 宽表id: {}", tableId);
            return;
        }

        //执行字段计算
        input = metricCalculate.getParam(input);

        //派生指标
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
                //执行前置过滤条件
                Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(input);
                if (Boolean.FALSE.equals(filter)) {
                    continue;
                }
                DeriveCalculateData deriveCalculateData = new DeriveCalculateData<>();
                deriveCalculateData.setData(input.clone());
                deriveCalculateData.setDeriveId(deriveMetricCalculate.getId());
                DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
                deriveCalculateData.setDimensionSet(dimensionSet);

                readOnlyContext.output(new OutputTag<>(DERIVE, TypeInformation.of(DeriveCalculateData.class)), deriveCalculateData);
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
                                        BroadcastProcessFunction<String, DataDetailsWideTable, Void>.Context context,
                                        Collector<Void> collector) throws Exception {
        BroadcastState<Long, MetricCalculate> broadcastState = context.getBroadcastState(mapStateDescriptor);
        broadcastState.put(dataDetailsWideTable.getId(), MetricUtil.initMetricCalculate(dataDetailsWideTable));
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        BroadcastState<Long, MetricCalculate> broadcastState = context.getOperatorStateStore().getBroadcastState(mapStateDescriptor);
        //如果是作业恢复, 重新初始化指标计算类
        if (context.isRestored()) {
            broadcastState.iterator().forEachRemaining(tempEntry -> tempEntry.setValue(MetricUtil.initMetricCalculate(tempEntry.getValue())));
        } else {
            //如果是作业初始化, 获取并且初始化所有指标计算类
            String jsonArray = HttpUtil.get(url);
            if (StrUtil.isBlank(jsonArray)) {
                return;
            }
            List<DataDetailsWideTable> list = JSONUtil.toList(jsonArray, DataDetailsWideTable.class);
            if (CollUtil.isEmpty(list)) {
                return;
            }
            Map<Long, MetricCalculate> tempMap = list.stream()
                    .map(MetricUtil::initMetricCalculate)
                    .collect(Collectors.toMap(DataDetailsWideTable::getId, Function.identity()));

            broadcastState.putAll(tempMap);
        }
    }

}
