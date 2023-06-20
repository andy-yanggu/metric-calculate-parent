package com.yanggu.metric_calculate.flink.process_function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;

@Slf4j
public class MyBroadcastProcessFunction extends BroadcastProcessFunction<String, DataDetailsWideTable, Object> {

    private final MapStateDescriptor<Long, DataDetailsWideTable> dataDetailsWideTableMapStateDescriptor =
            new MapStateDescriptor<>("DataDetailsWideTable", Long.class, DataDetailsWideTable.class);

    @Override
    public void processElement(String jsonString, BroadcastProcessFunction<String, DataDetailsWideTable, Object>.ReadOnlyContext readOnlyContext, Collector<Object> collector) throws Exception {
        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
        Long tableId = jsonObject.getLong("tableId");
        if (tableId == null) {
            return;
        }
        ReadOnlyBroadcastState<Long, DataDetailsWideTable> broadcastState = readOnlyContext.getBroadcastState(dataDetailsWideTableMapStateDescriptor);
        DataDetailsWideTable dataDetailsWideTable = broadcastState.get(tableId);
        if (dataDetailsWideTable == null) {
            log.error("广播状态中没有数据明细宽表数据: 宽表id: {}", tableId);
            return;
        }
        List<Derive> deriveList = dataDetailsWideTable.getDerive();
        if (CollUtil.isNotEmpty(deriveList)) {
            for (Derive derive : deriveList) {
                JSONObject clone = jsonObject.clone();
                clone.set("deriveId", derive.getId());
                readOnlyContext.output(new OutputTag<>("derive"), clone);
            }
        }
        List<Global> globalList = dataDetailsWideTable.getGlobal();
        if (CollUtil.isNotEmpty(globalList)) {
            for (Global global : globalList) {
                JSONObject clone = jsonObject.clone();
                clone.set("globalId", global.getId());
                readOnlyContext.output(new OutputTag<>("global"), clone);
            }
        }
    }

    @Override
    public void processBroadcastElement(DataDetailsWideTable dataDetailsWideTable, BroadcastProcessFunction<String, DataDetailsWideTable, Object>.Context context, Collector<Object> collector) throws Exception {
        BroadcastState<Long, DataDetailsWideTable> broadcastState = context.getBroadcastState(dataDetailsWideTableMapStateDescriptor);
        broadcastState.put(dataDetailsWideTable.getId(), dataDetailsWideTable);
    }
}
