package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.flink.pojo.DeriveConfigData;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.flink.util.Constant.DERIVE_CONFIG;

/**
 * 数据明细宽表处理函数
 */
public class DataTableProcessFunction extends ProcessFunction<DataDetailsWideTable, Void> {

    private static final long serialVersionUID = -4721794115378342971L;

    @Override
    public void processElement(DataDetailsWideTable dataDetailsWideTable,
                               ProcessFunction<DataDetailsWideTable, Void>.Context ctx,
                               Collector<Void> out) {

        MetricCalculate metricCalculate = BeanUtil.copyProperties(dataDetailsWideTable, MetricCalculate.class);
        MetricUtil.setFieldMap(metricCalculate);
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        Long tableId = metricCalculate.getId();
        List<String> aviatorFunctionJarPathList = metricCalculate.getAviatorFunctionJarPathList();
        List<String> udafJarPathList = metricCalculate.getUdafJarPathList();

        List<Derive> deriveList = dataDetailsWideTable.getDeriveList();
        if (CollUtil.isNotEmpty(deriveList)) {
            deriveList.forEach(tempDerive -> {
                DeriveConfigData deriveConfigData = new DeriveConfigData<>();
                deriveConfigData.setTableId(tableId);
                deriveConfigData.setFieldMap(fieldMap);
                deriveConfigData.setDerive(tempDerive);
                deriveConfigData.setAviatorFunctionJarPathList(aviatorFunctionJarPathList);
                deriveConfigData.setUdafJarPathList(udafJarPathList);
                ctx.output(new OutputTag<>(DERIVE_CONFIG, TypeInformation.of(DeriveConfigData.class)), deriveConfigData);
            });
        }
        List<Global> globalList = dataDetailsWideTable.getGlobalList();
        if (CollUtil.isNotEmpty(globalList)) {
            //TODO 全局指标
        }
    }

}
