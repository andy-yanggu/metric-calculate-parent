package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.pojo.metric.RoundAccuracy;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core2.table.PatternTable;
import com.yanggu.metric_calculate.core2.table.Table;
import com.yanggu.metric_calculate.core2.table.TableFactory;
import com.yanggu.metric_calculate.core2.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 派生指标计算类
 *
 * @param <IN>  聚合函数输入值
 * @param <ACC> 中间状态数据
 * @param <OUT> 输出值
 */
@Data
@Slf4j
@NoArgsConstructor
public class DeriveMetricCalculate<IN, ACC, OUT> {

    /**
     * 派生指标的id
     */
    private Long id;

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

    private TableFactory<IN, ACC, OUT> tableFactory;

    /**
     * 聚合函数字段处理器
     */
    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

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

    /**
     * 滚动时间窗口、滑动时间窗口、滑动计数窗口、状态窗口、全窗口、会话窗口
     */
    private int windowType;

    /**
     * 是否是CEP类型
     */
    private Boolean isCep;

    /**
     * 有状态计算
     *
     * @param input
     * @return
     */
    @SneakyThrows
    public List<DeriveMetricCalculateResult<OUT>> stateExec(JSONObject input) {
        //执行前置过滤条件
        Boolean filter = filterFieldProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return Collections.emptyList();
        }

        //提取出时间字段
        Long timestamp = timeFieldProcessor.process(input);

        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //查询外部数据
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);
        if (historyMetricCube == null) {
            historyMetricCube = createMetricCube(dimensionSet);
        } else {
            tableFactory.setTable(historyMetricCube.getTable());
        }
        Table<IN, ACC, OUT> timeTable = historyMetricCube.getTable();

        //放入明细数据进行累加
        if (Boolean.TRUE.equals(isCep)) {
            //如果是CEP类型的, 进行特殊处理
            ((PatternTable<IN, ACC, OUT>) timeTable).put(timestamp, input);
        } else {
            //提取出度量值
            IN in = aggregateFieldProcessor.process(input);
            timeTable.put(timestamp, in);
        }
        deriveMetricMiddleStore.update(historyMetricCube);
        return query(historyMetricCube, timestamp);
    }

    /**
     * 无状态计算
     * <p>区别在于是否考虑当前笔</p>
     * <p>是否写入到外部存储中</p>
     *
     * @param input 明细数据
     * @return
     */
    @SneakyThrows
    public List<DeriveMetricCalculateResult<OUT>> noStateExec(JSONObject input) {
        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //提取出时间字段
        Long timestamp = timeFieldProcessor.process(input);

        //查询外部数据
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);

        //包含当前笔需要执行前置过滤条件
        if (Boolean.TRUE.equals(includeCurrent) && Boolean.TRUE.equals(filterFieldProcessor.process(input))) {
            if (historyMetricCube == null) {
                historyMetricCube = createMetricCube(dimensionSet);
            } else {
                tableFactory.setTable(historyMetricCube.getTable());
            }
            //放入明细数据进行累加
            if (Boolean.TRUE.equals(isCep)) {
                ((PatternTable<IN, ACC, OUT>) historyMetricCube.getTable()).put(timestamp, input);
            } else {
                IN in = aggregateFieldProcessor.process(input);
                historyMetricCube.getTable().put(timestamp, in);
            }
        }
        if (historyMetricCube == null) {
            return Collections.emptyList();
        }
        return query(historyMetricCube, timestamp);
    }

    /**
     * 查询操作, 查询出指标数据
     *
     * @param metricCube
     * @return
     */
    public List<DeriveMetricCalculateResult<OUT>> query(MetricCube<IN, ACC, OUT> metricCube,
                                                        Long timestamp) {
        //获取统计的时间窗口
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        if (CollUtil.isEmpty(timeWindowList)) {
            return Collections.emptyList();
        }

        List<DeriveMetricCalculateResult<OUT>> list = new ArrayList<>();
        for (TimeWindow timeWindow : timeWindowList) {

            //窗口开始时间
            long windowStart = timeWindow.getWindowStart();
            //窗口结束时间
            long windowEnd = timeWindow.getWindowEnd();

            //聚合值
            OUT query = metricCube.getTable().query();

            if (query == null) {
                continue;
            }

            DeriveMetricCalculateResult<OUT> result = new DeriveMetricCalculateResult<>();
            //指标key
            result.setKey(metricCube.getDimensionSet().getKey());

            //指标名称
            result.setName(metricCube.getDimensionSet().getMetricName());

            //指标维度
            result.setDimensionMap(((LinkedHashMap) metricCube.getDimensionSet().getDimensionMap()));

            result.setStartTime(DateUtils.formatDateTime(windowStart));

            result.setEndTime(DateUtils.formatDateTime(windowEnd));

            //聚合值
            result.setResult(query);

            list.add(result);
        }
        return list;
    }

    private MetricCube<IN, ACC, OUT> createMetricCube(DimensionSet dimensionSet) {
        MetricCube<IN, ACC, OUT> metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        Table<IN, ACC, OUT> table = tableFactory.createTable();
        metricCube.setTable(table);
        return metricCube;
    }

}
