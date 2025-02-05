package com.yanggu.metric_calculate.config.service.impl;

import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.service.SimulateMetricCalculateService;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiFunction;

@Service
public class SimulateMetricCalculateServiceImpl implements SimulateMetricCalculateService {

    @Autowired
    private ModelService modelService;

    @Autowired
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> noStateCalculate(Map<String, Object> input,
                                                                            Integer modelId,
                                                                            Integer deriveId) {
        //进行无状态计算
        BiFunction<DeriveMetricCalculate<IN, ACC, OUT>, Map<String, Object>, DeriveMetricCalculateResult<OUT>> function = DeriveMetricCalculate::noStateExec;
        return deriveMetricCalculate(input, modelId, deriveId, function);
    }

    @Override
    public <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> stateCalculate(Map<String, Object> input, Integer modelId, Integer deriveId) {
        //进行有状态计算
        BiFunction<DeriveMetricCalculate<IN, ACC, OUT>, Map<String, Object>, DeriveMetricCalculateResult<OUT>> function = DeriveMetricCalculate::stateExec;
        return deriveMetricCalculate(input, modelId, deriveId, function);
    }

    private <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> deriveMetricCalculate(
            Map<String, Object> input,
                                                                     Integer modelId,
                                                                     Integer deriveId,
                                                                     BiFunction<DeriveMetricCalculate<IN, ACC, OUT>, Map<String, Object>, DeriveMetricCalculateResult<OUT>> function) {
        //获取核心宽表类
        Model coreModel = modelService.toCoreModel(modelId);
        //初始化指标计算类
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(coreModel);
        //获取指标计算类
        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(deriveId.longValue());
        //设置外部存储
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);
        //进行字段计算
        Map<String, Object> param = metricCalculate.getParam(input);
        //进行派生指标计算
        return function.apply(deriveMetricCalculate, param);
    }

}
