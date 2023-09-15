package com.yanggu.metric_calculate.config.service.impl;

import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.service.SimulateMetricCalculateService;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class SimulateMetricCalculateServiceImpl implements SimulateMetricCalculateService {

    @Autowired
    private ModelService modelService;

    @Autowired
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public <R> DeriveMetricCalculateResult<R> noStateCalculate(JSONObject input, Integer modelId, Integer deriveId) {
        //进行无状态计算
        BiFunction<DeriveMetricCalculate<Object, Object, R>, JSONObject, DeriveMetricCalculateResult<R>> function = DeriveMetricCalculate::noStateExec;
        return deriveMetricCalculate(input, modelId, deriveId, function);
    }

    @Override
    public <R> DeriveMetricCalculateResult<R> stateCalculate(JSONObject input, Integer modelId, Integer deriveId) {
        //进行有状态计算
        BiFunction<DeriveMetricCalculate<Object, Object, R>, JSONObject, DeriveMetricCalculateResult<R>> function = DeriveMetricCalculate::stateExec;
        return deriveMetricCalculate(input, modelId, deriveId, function);
    }

    private <R> DeriveMetricCalculateResult<R> deriveMetricCalculate(JSONObject input,
                                                                     Integer modelId,
                                                                     Integer deriveId,
                                                                     BiFunction<DeriveMetricCalculate<Object, Object, R>, JSONObject, DeriveMetricCalculateResult<R>> function) {
        //获取核心宽表类
        Model coreModel = modelService.toCoreModel(modelId);
        //初始化指标计算类
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(coreModel);
        //获取指标计算类
        DeriveMetricCalculate<Object, Object, R> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(deriveId.longValue());
        //设置外部存储
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);
        //进行字段计算
        JSONObject param = metricCalculate.getParam(input);
        //进行派生指标计算
        return function.apply(deriveMetricCalculate, param);
    }

}
