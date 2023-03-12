package com.yanggu.metric_calculate.core.field_process.metric;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 度量值字段处理器, 从输入的明细数据中，提取出度量值
 */
@Data
@Slf4j
@NoArgsConstructor
public class MetricFieldProcessor<T, R> implements FieldProcessor<T, R> {

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 度量值表达式
     */
    private String metricExpress;

    /**
     * 度量字段表达式
     */
    private Expression metricExpression;

    @Override
    public void init() throws Exception {
        if (StrUtil.isBlank(metricExpress)) {
            throw new RuntimeException("度量表达式为空");
        }
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        //编译度量字段表达式
        Expression tempMetricExpression = instance.compile(metricExpress, true);
        List<String> variableNames = tempMetricExpression.getVariableNames();
        //检查数据明细宽表中是否包含当前参数
        if (CollUtil.isNotEmpty(variableNames)) {
            variableNames.forEach(tempName -> {
                if (!fieldMap.containsKey(tempName)) {
                    throw new RuntimeException("数据明细宽表中没有该度量字段: " + tempName);
                }
            });
        }
        this.metricExpression = tempMetricExpression;
    }

    @Override
    @SneakyThrows
    public R process(T input2) {
        JSONObject input = JSONUtil.parseObj(input2);
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);

        return (R) metricExpression.execute(params);
    }

}
