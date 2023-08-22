package com.yanggu.metric_calculate.config.service.impl;

import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.service.DeriveService;
import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.service.SimulateMetricCalculateService;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.enums.BasicType;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.dromara.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.DERIVE_ID_ERROR;
import static com.yanggu.metric_calculate.config.enums.ResultCode.MODEL_ID_ERROR;

@Service
public class SimulateMetricCalculateServiceImpl implements SimulateMetricCalculateService {

    @Autowired
    private ModelService modelService;

    @Autowired
    private DeriveService deriveService;

    @Autowired
    private DeriveMapstruct deriveMapstruct;

    @Autowired
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public <R> DeriveMetricCalculateResult<R> noStateCalculate(JSONObject input, Integer modelId, Integer deriveId) throws Exception {
        ModelDto modelDto = modelService.queryById(modelId);
        if (modelDto == null) {
            throw new BusinessException(MODEL_ID_ERROR, modelId);
        }
        List<ModelColumnDto> modelColumnList = modelDto.getModelColumnList();
        List<ModelColumn> collect = modelColumnList.stream()
                .map(tempModelColumn -> {
                    ModelColumn modelColumn = new ModelColumn();
                    modelColumn.setName(tempModelColumn.getName());
                    modelColumn.setDataType(BasicType.valueOf(tempModelColumn.getDataType().name()));
                    return modelColumn;
                })
                .collect(Collectors.toList());
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(collect);
        DeriveDto deriveDto = deriveService.queryById(deriveId);
        if (deriveDto == null) {
            throw new BusinessException(DERIVE_ID_ERROR, deriveId);
        }
        DeriveMetrics deriveMetrics = null;
        AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory();
        aviatorFunctionFactory.init();
        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory();
        aggregateFunctionFactory.init();
        DeriveMetricCalculate<?, ?, R> deriveMetricCalculate = MetricUtil.initDerive(deriveMetrics, Long.valueOf(modelId), fieldMap, aviatorFunctionFactory, aggregateFunctionFactory);
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);
        return deriveMetricCalculate.noStateExec(input);
    }

}
