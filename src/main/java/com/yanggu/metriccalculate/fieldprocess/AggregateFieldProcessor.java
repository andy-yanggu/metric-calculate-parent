package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.unit.UnitFactory;
import com.yanggu.metriccalculate.util.MetricUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 聚合字段处理器
 */
@Data
@Slf4j
public class AggregateFieldProcessor<M extends MergedUnit<M>> implements FieldExtractProcessor<JSONObject, M> {

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 度量值表达式
     */
    private String metricExpress;

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 度量字段表达式
     */
    private Expression metricExpression;

    @Override
    public void init() throws Exception {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        //编译度量字段表达式
        Expression tempMetricExpression = instance.compile(metricExpress, true);
        List<String> variableNames = tempMetricExpression.getVariableNames();
        //检查数据明细宽表中是否包含当前参数
        if (CollUtil.isNotEmpty(variableNames)) {
            variableNames.forEach(tempName -> {
                if (!fieldMap.containsKey(tempName)) {
                    throw new RuntimeException("数据明细宽表中没有该度量值: " + tempName);
                }
            });
        }
        this.metricExpression = tempMetricExpression;
    }

    @Override
    public M process(JSONObject input) throws Exception {
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);
        if (log.isDebugEnabled()) {
            log.debug("度量字段表达式: {}, 输入的数据: {}", metricExpress, JSONUtil.toJsonStr(params));
        }

        //获取度量值
        Object execute = metricExpression.execute(params);

        //生成MergedUnit
        return (M) UnitFactory.initInstanceByValue(aggregateType, execute);
    }

}
