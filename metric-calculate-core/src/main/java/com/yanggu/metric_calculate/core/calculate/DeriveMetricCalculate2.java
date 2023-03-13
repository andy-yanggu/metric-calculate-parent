package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.aggregate.AggregateProcessor;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.RoundAccuracy;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core.pojo.store.StoreInfo;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.AggregateFunction;
import com.yanggu.metric_calculate.core.util.RoundAccuracyUtil;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
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
public class DeriveMetricCalculate2<T, IN, ACC, OUT> {

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
    private FilterFieldProcessor<T> filterFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor<T> timeFieldProcessor;

    /**
     * 时间聚合粒度。包含时间单位和时间长度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 维度字段处理器, 从明细数据中提取出维度数据
     */
    private DimensionSetProcessor<T> dimensionSetProcessor;

    private AggregateProcessor<T, IN, ACC, OUT> aggregateProcessor;

    /**
     * 是否包含当前笔, 默认包含
     */
    private Boolean includeCurrent = true;

    /**
     * 派生指标中间结算结果存储
     */
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    /**
     * 精度数据
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 是否是自定义udaf
     */
    private Boolean isUdaf;

    private Map<DimensionSet, TreeMap<Long, ACC>> map;

    public void exec(T input) {
        Boolean filter = filterFieldProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return;
        }
        Long timestamp = timeFieldProcessor.process(input);
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //查询外部数据源
        TreeMap<Long, ACC> treeMap = map.computeIfAbsent(dimensionSet, k -> new TreeMap<>());
        Long aggregateTimestamp = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        ACC historyAcc = treeMap.get(aggregateTimestamp);
        ACC nowAcc = aggregateProcessor.exec(historyAcc, input);
        treeMap.put(aggregateTimestamp, nowAcc);
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(timestamp);
        for (TimeWindow window : timeWindow) {
            Collection<ACC> values = treeMap.subMap(window.getWindowStart(), true, window.getWindowEnd(), false).values();
            OUT mergeResult = aggregateProcessor.getMergeResult(new ArrayList<>(values));
            System.out.println("维度信息:" + dimensionSet.realKey() +
                    ", 窗口开始时间: " + DateUtil.formatDateTime(new Date(window.getWindowStart())) +
                    ", 窗口结束时间: " + DateUtil.formatDateTime(new Date(window.getWindowEnd())) +
                    ", 聚合值: " + mergeResult);
        }
    }

}
