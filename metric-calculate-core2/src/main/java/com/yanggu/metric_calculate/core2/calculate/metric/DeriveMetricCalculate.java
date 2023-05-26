package com.yanggu.metric_calculate.core2.calculate.metric;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.pojo.metric.RoundAccuracy;
import com.yanggu.metric_calculate.core2.table.Table;
import com.yanggu.metric_calculate.core2.table.TableFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

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
     * 添加度量值
     *
     * @param input
     * @param historyMetricCube
     * @return
     */
    public MetricCube<IN, ACC, OUT> addInput(JSONObject input, MetricCube<IN, ACC, OUT> historyMetricCube) {
        if (historyMetricCube == null) {
            //提取出维度字段
            DimensionSet dimensionSet = dimensionSetProcessor.process(input);
            historyMetricCube = createMetricCube(dimensionSet);
        } else {
            tableFactory.setTable(historyMetricCube.getTable());
        }
        //放入明细数据进行累加
        historyMetricCube.getTable().put(input);
        return historyMetricCube;
    }

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

        //添加度量值
        historyMetricCube = addInput(input, historyMetricCube);

        //更新到外部存储
        deriveMetricMiddleStore.update(historyMetricCube);

        //查询数据, 并返回
        return historyMetricCube.query(input);
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
            //添加度量值
            historyMetricCube = addInput(input, historyMetricCube);
        }
        if (historyMetricCube == null) {
            return null;
        }
        return historyMetricCube.query(input);
    }

    public CompletableFuture<DeriveMetricCalculateResult<OUT>> noStateFutureExec(
                                                        JSONObject input,
                                                        CompletableFuture<MetricCube<IN, ACC, OUT>> completableFuture) {
        return completableFuture.thenApply(historyMetricCube -> {
            //包含当前笔需要执行前置过滤条件
            if (Boolean.TRUE.equals(includeCurrent) && Boolean.TRUE.equals(filterFieldProcessor.process(input))) {
                historyMetricCube = addInput(input, historyMetricCube);
            }
            if (historyMetricCube == null) {
                return null;
            }
            return historyMetricCube.query(input);
        });
    }

    public MetricCube<IN, ACC, OUT> createMetricCube(DimensionSet dimensionSet) {
        MetricCube<IN, ACC, OUT> metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        Table<IN, ACC, OUT> table = tableFactory.createTable();
        metricCube.setTable(table);
        return metricCube;
    }

}
