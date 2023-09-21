package com.yanggu.metric_calculate.core.window;


import com.yanggu.metric_calculate.core.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.STATUS_WINDOW;

/**
 * 状态窗口, 当窗口字段的值发生改变时, 生成一个新的窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class StatusWindow<IN, ACC, OUT> extends AbstractWindow<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private List<AviatorExpressParam> statusExpressParamList;

    private List<String> statusExpressList;

    private MetricListFieldProcessor metricListFieldProcessor;

    private List<Object> statusList;

    private List<IN> inList = new ArrayList<>();

    @Override
    public void init() {
        this.metricListFieldProcessor = FieldProcessorUtil.getMetricListFieldProcessor(fieldMap, statusExpressParamList, aviatorFunctionFactory);
    }

    @Override
    public WindowTypeEnum type() {
        return STATUS_WINDOW;
    }

    @Override
    public void put(JSONObject input) {
        List<Object> newStatusList = metricListFieldProcessor.process(input);
        //如果状态不相同, 清空数据
        if (!newStatusList.equals(statusList)) {
            inList.clear();
        }
        this.statusList = newStatusList;
        //添加度量值
        inList.add(getInFromInput(input));
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        OUT outFromInList = aggregateFieldProcessor.getOutFromInList(inList);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setStatusList(statusList);
        deriveMetricCalculateResult.setResult(outFromInList);
        return deriveMetricCalculateResult;
    }

    //@Override
    public StatusWindow<IN, ACC, OUT> merge(StatusWindow<IN, ACC, OUT> thatTable) {

        StatusWindow<IN, ACC, OUT> statusWindowTable = new StatusWindow<>();
        //inList.addAll(thatTable.getInList());
        //statusWindowTable.setInList(inList);
        return statusWindowTable;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(inList);
    }

}
