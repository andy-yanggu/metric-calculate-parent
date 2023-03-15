package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateProcessor;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.RoundAccuracy;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core2.table.TimeTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 派生指标计算类
 */
@Data
@Slf4j
@NoArgsConstructor
public class DeriveMetricCalculate<IN, ACC, OUT> {

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 前置过滤条件处理器, 进行过滤处理
     */
    private FilterFieldProcessor filterFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 时间聚合粒度。包含时间单位和时间长度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 维度字段处理器, 从明细数据中提取出维度数据
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 提取出度量数据
     */
    private FieldProcessor<JSONObject, IN> metricFieldProcessor;

    private AggregateProcessor<IN, ACC, OUT> aggregateProcessor;

    /**
     * 中间状态数据外部存储
     */
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    /**
     * 是否包含当前笔, 默认包含
     */
    private Boolean includeCurrent = true;

    /**
     * 精度数据
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 是否是自定义udaf
     */
    private Boolean isUdaf;

    @SneakyThrows
    public List<OUT> stateExec(JSONObject input) {
        //执行前置过滤条件
        Boolean filter = filterFieldProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return Collections.emptyList();
        }

        //提取出度量值
        IN process = metricFieldProcessor.process(input);
        if (process == null) {
            return Collections.emptyList();
        }

        //提取出时间字段
        Long timestamp = timeFieldProcessor.process(input);

        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //查询外部数据
        MetricCube<IN, ACC, OUT> metricCube = deriveMetricMiddleStore.get(dimensionSet);
        if (metricCube == null) {
            metricCube = new MetricCube<>();
            metricCube.setDimensionSet(dimensionSet);
            TimeTable<IN, ACC, OUT> timeTable = new TimeTable<>();
            metricCube.setTimeTable(timeTable);
        }
        TimeTable<IN, ACC, OUT> timeTable = metricCube.getTimeTable();
        timeTable.setAggregateProcessor(aggregateProcessor);
        timeTable.setTimeBaselineDimension(timeBaselineDimension);

        //放入明细数据进行累加
        timeTable.put(timestamp, process);

        List<OUT> list = new ArrayList<>();
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(timestamp);
        for (TimeWindow window : timeWindow) {
            OUT mergeResult = timeTable.query(window.getWindowStart(), true, window.getWindowEnd(), false);
            if (mergeResult != null) {
                list.add(mergeResult);
            }
        }
        deriveMetricMiddleStore.update(metricCube);
        return list;
    }

    @SneakyThrows
    public List<OUT> noStateExec(JSONObject input) {
        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);
        Long timestamp = timeFieldProcessor.process(input);

        //包含当前笔需要执行前置过滤条件
        if (Boolean.TRUE.equals(includeCurrent) && Boolean.TRUE.equals(filterFieldProcessor.process(input))) {
            IN process = metricFieldProcessor.process(input);
            if (process != null) {
                if (historyMetricCube == null) {
                    historyMetricCube = new MetricCube<>();
                    historyMetricCube.setDimensionSet(dimensionSet);
                    TimeTable<IN, ACC, OUT> timeTable = new TimeTable<>();
                    historyMetricCube.setTimeTable(timeTable);
                }
                TimeTable<IN, ACC, OUT> timeTable = historyMetricCube.getTimeTable();
                timeTable.setAggregateProcessor(aggregateProcessor);
                timeTable.setTimeBaselineDimension(timeBaselineDimension);

                //放入明细数据进行累加
                timeTable.put(timestamp, process);
            }
        }
        if (historyMetricCube == null) {
            return Collections.emptyList();
        }
        List<OUT> list = new ArrayList<>();
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(timestamp);
        TimeTable<IN, ACC, OUT> timeTable = historyMetricCube.getTimeTable();
        for (TimeWindow window : timeWindow) {
            OUT mergeResult = timeTable
                    .query(window.getWindowStart(), true, window.getWindowEnd(), false);
            if (mergeResult != null) {
                list.add(mergeResult);
            }
        }
        return list;
    }

}
