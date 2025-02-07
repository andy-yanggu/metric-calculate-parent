package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.RoundAccuracy;
import com.yanggu.metric_calculate.core.window.AbstractWindow;
import com.yanggu.metric_calculate.core.window.WindowFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.lang.mutable.MutableObj;

import java.util.Map;
import java.util.function.Consumer;

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
     * 窗口工厂类
     * <p>用于初始化窗口和给窗口实现类字段赋值</p>
     */
    private WindowFactory<IN, ACC, OUT> windowFactory;

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
     * 无状态计算
     * <p>直接读外部存储, 不写入外部存储</p>
     * <p>需要是否考虑当前笔</p>
     *
     * @param input 明细数据
     * @return
     */
    @SneakyThrows
    public DeriveMetricCalculateResult<OUT> noStateExec(Map<String, Object> input) {
        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //查询外部数据
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);

        //考虑是否包含当前笔
        return noStateExec(input, historyMetricCube, dimensionSet);
    }

    /**
     * 无状态计算
     * <p>外部数据直接传入, 而不是自己读取</p>
     * <p>需要是否考虑当前笔</p>
     *
     * @param input 明细数据
     * @return
     */
    @SneakyThrows
    public DeriveMetricCalculateResult<OUT> noStateExec(Map<String, Object> input,
                                                        MetricCube<IN, ACC, OUT> historyMetricCube,
                                                        DimensionSet dimensionSet) {
        //包含当前笔且前置过滤条件为true
        if (Boolean.TRUE.equals(includeCurrent) && Boolean.TRUE.equals(filterFieldProcessor.process(input))) {
            //添加度量值
            historyMetricCube = addInput(input, historyMetricCube, dimensionSet);
            return historyMetricCube.query();
        } else {
            return query(historyMetricCube, input);
        }
    }

    /**
     * 有状态计算
     * <p>直接读写外部数据</p>
     *
     * @param input 明细数据
     * @return
     */
    @SneakyThrows
    public DeriveMetricCalculateResult<OUT> stateExec(Map<String, Object> input) {
        //提取出维度字段
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //根据维度查询外部数据
        MetricCube<IN, ACC, OUT> historyMetricCube = deriveMetricMiddleStore.get(dimensionSet);
        MutableObj<DeriveMetricCalculateResult<OUT>> result = new MutableObj<>();
        //前置过滤条件为false和true对应的消费逻辑
        //为false时直接查询
        Consumer<MetricCube<IN, ACC, OUT>> filterFalseConsumer = oldMetricCube -> {
            DeriveMetricCalculateResult<OUT> query = query(oldMetricCube, input);
            result.set(query);
        };
        //为true时更新到外部存储后查询
        Consumer<MetricCube<IN, ACC, OUT>> filterTrueConsumer = newMetricCube -> {
            if (newMetricCube != null && !newMetricCube.isEmpty()) {
                try {
                    deriveMetricMiddleStore.update(newMetricCube);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                DeriveMetricCalculateResult<OUT> query = newMetricCube.query();
                result.set(query);
            }
        };
        stateExec(input, historyMetricCube, dimensionSet, filterFalseConsumer, filterTrueConsumer);
        return result.get();
    }

    /**
     * 有状态计算
     *
     * @param input 明细数据
     * @return
     */
    @SneakyThrows
    public void stateExec(Map<String, Object> input,
                          MetricCube<IN, ACC, OUT> historyMetricCube,
                          DimensionSet dimensionSet,
                          Consumer<MetricCube<IN, ACC, OUT>> filterFalseConsumer,
                          Consumer<MetricCube<IN, ACC, OUT>> filterTrueConsumer) {

        //执行前置过滤条件
        Boolean filter = filterFieldProcessor.process(input);
        //如果为false，返回历史数据即可
        if (Boolean.FALSE.equals(filter)) {
            //执行为false的逻辑
            filterFalseConsumer.accept(historyMetricCube);
        } else {
            //如果为true, 需要将度量值添加到历史数据中
            MetricCube<IN, ACC, OUT> newMetricCube = addInput(input, historyMetricCube, dimensionSet);
            //执行为true的逻辑
            filterTrueConsumer.accept(newMetricCube);
        }
    }

    /**
     * 根据维度查询数据
     *
     * @param dimensionSet
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query(DimensionSet dimensionSet) throws Exception {
        MetricCube<IN, ACC, OUT> metricCube = deriveMetricMiddleStore.get(dimensionSet);
        if (metricCube == null) {
            return null;
        }
        windowFactory.setWindow(metricCube.getWindow());
        return metricCube.query();
    }

    /**
     * 查询实时数据
     *
     * @param metricCube
     * @param input
     * @return
     */
    public DeriveMetricCalculateResult<OUT> query(MetricCube<IN, ACC, OUT> metricCube, Map<String, Object> input) {
        if (metricCube == null) {
            return null;
        }
        windowFactory.setWindow(metricCube.getWindow());
        return metricCube.query(input);
    }

    /**
     * 添加度量值
     *
     * @param input
     * @param historyMetricCube
     * @param dimensionSet
     * @return
     */
    public MetricCube<IN, ACC, OUT> addInput(Map<String, Object> input,
                                             MetricCube<IN, ACC, OUT> historyMetricCube,
                                             DimensionSet dimensionSet) {
        if (historyMetricCube == null) {
            historyMetricCube = new MetricCube<>();
            historyMetricCube.setDimensionSet(dimensionSet);
            AbstractWindow<IN, ACC, OUT> window = windowFactory.createWindow();
            historyMetricCube.setWindow(window);
        } else {
            windowFactory.setWindow(historyMetricCube.getWindow());
        }
        //放入明细数据进行累加
        historyMetricCube.put(input);
        //删除过期数据
        historyMetricCube.deleteData();
        return historyMetricCube;
    }

}
