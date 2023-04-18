package com.yanggu.metric_calculate.core2.calculate;


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
import com.yanggu.metric_calculate.core2.table.PatternTable;
import com.yanggu.metric_calculate.core2.table.Table;
import com.yanggu.metric_calculate.core2.table.TableFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

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
     * 维度字段处理器, 从明细数据中提取出维度数据
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 数据切分工厂类
     */
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
    public DeriveMetricCalculateResult<OUT> stateExec(JSONObject input) {
        //执行前置过滤条件
        Boolean filter = filterFieldProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return null;
        }

        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //根据维度查询外部数据
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);
        if (historyMetricCube == null) {
            historyMetricCube = createMetricCube(dimensionSet);
        } else {
            tableFactory.setTable(historyMetricCube.getTable());
        }

        //放入明细数据进行累加
        addInput(input, historyMetricCube);

        //更新到外部存储
        deriveMetricMiddleStore.update(historyMetricCube);
        return query(historyMetricCube);
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
    public DeriveMetricCalculateResult<OUT> noStateExec(JSONObject input) {
        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //查询外部数据
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);

        //包含当前笔需要执行前置过滤条件
        if (Boolean.TRUE.equals(includeCurrent) && Boolean.TRUE.equals(filterFieldProcessor.process(input))) {
            if (historyMetricCube == null) {
                historyMetricCube = createMetricCube(dimensionSet);
            } else {
                tableFactory.setTable(historyMetricCube.getTable());
            }
            addInput(input, historyMetricCube);
        }
        if (historyMetricCube == null) {
            return null;
        }
        return query(historyMetricCube);
    }

    /**
     * 放入明细数据进行累加
     *
     * @param input
     * @param historyMetricCube
     */
    private void addInput(JSONObject input, MetricCube<IN, ACC, OUT> historyMetricCube) {
        //提取出时间字段
        Long timestamp = timeFieldProcessor.process(input);
        if (Boolean.TRUE.equals(isCep)) {
            ((PatternTable<IN, ACC, OUT>) historyMetricCube.getTable()).put(timestamp, input);
        } else {
            IN in = aggregateFieldProcessor.process(input);
            historyMetricCube.getTable().put(timestamp, in);
        }
    }

    /**
     * 查询操作, 查询出指标数据
     *
     * @param metricCube
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query(MetricCube<IN, ACC, OUT> metricCube) {
        //聚合值
        OUT query = metricCube.getTable().query();

        if (query == null) {
            return null;
        }

        DeriveMetricCalculateResult<OUT> result = new DeriveMetricCalculateResult<>();
        //指标key
        result.setKey(metricCube.getDimensionSet().getKey());

        //指标名称
        result.setName(metricCube.getDimensionSet().getMetricName());

        //指标维度
        result.setDimensionMap(((LinkedHashMap) metricCube.getDimensionSet().getDimensionMap()));

        //聚合值
        result.setResult(query);
        return result;
    }

    private MetricCube<IN, ACC, OUT> createMetricCube(DimensionSet dimensionSet) {
        MetricCube<IN, ACC, OUT> metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        Table<IN, OUT> table = tableFactory.createTable();
        metricCube.setTable(table);
        return metricCube;
    }

}
