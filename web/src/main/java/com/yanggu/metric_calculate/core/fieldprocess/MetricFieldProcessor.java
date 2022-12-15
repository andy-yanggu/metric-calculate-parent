package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 度量字段处理器, 从输入的明细数据中，提取出度量值
 */
@Data
@Slf4j
@NoArgsConstructor
public class MetricFieldProcessor<R> implements FieldExtractProcessor<JSONObject, R> {

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
    protected transient Expression metricExpression;

    public MetricFieldProcessor(Map<String, Class<?>> fieldMap, String metricExpress) {
        this.fieldMap = fieldMap;
        this.metricExpress = metricExpress;
    }

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
                    throw new RuntimeException("数据明细宽表中没有该度量字段: " + tempName);
                }
            });
        }
        this.metricExpression = tempMetricExpression;
    }

    @Override
    public R process(JSONObject input) throws Exception {
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);

        R execute = (R) metricExpression.execute(params);
        if (log.isDebugEnabled()) {
            log.debug("度量字段表达式: {}, 输入的数据: {}, 生成数据: {}", metricExpress, JSONUtil.toJsonStr(params), StrUtil.toString(execute));
        }
        return execute;
    }

}
