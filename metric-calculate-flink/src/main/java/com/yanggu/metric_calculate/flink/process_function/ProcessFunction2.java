package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

public class ProcessFunction2 extends ProcessFunction<DataDetailsWideTable, Void> {

    @Override
    public void processElement(DataDetailsWideTable dataDetailsWideTable, ProcessFunction<DataDetailsWideTable, Void>.Context ctx, Collector<Void> out) throws Exception {
        List<Derive> deriveList = dataDetailsWideTable.getDerive();

        MetricCalculate metricCalculate = BeanUtil.copyProperties(dataDetailsWideTable, MetricCalculate.class);
        MetricUtil.setFieldMap(metricCalculate);
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        Long tableId = metricCalculate.getId();

        if (CollUtil.isNotEmpty(deriveList)) {
            deriveList.forEach(tempDerive -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("tableId", tableId);
                jsonObject.set("fieldMap", fieldMap);
                jsonObject.set("derive", tempDerive);
                ctx.output(new OutputTag<>("derive-config"), jsonObject);
            });
        }
        List<Global> globalList = dataDetailsWideTable.getGlobal();
        if (CollUtil.isNotEmpty(globalList)) {
            //TODO 全局指标
        }
    }
}
