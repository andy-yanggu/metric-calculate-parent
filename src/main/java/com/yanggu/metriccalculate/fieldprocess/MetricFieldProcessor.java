package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metriccalculate.util.MetricUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private Expression metricExpression;

    public MetricFieldProcessor(Map<String, Class<?>> fieldMap, String metricExpress) {
        this.fieldMap = fieldMap;
        this.metricExpress = metricExpress;
    }

    @Override
    public void init() throws Exception {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        //编译度量字段表达式
        metricExpression = instance.compile(metricExpress, true);
    }

    @Override
    public R process(JSONObject input) {
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);
        if (log.isDebugEnabled()) {
            log.debug("度量字段表达式: {}, 输入的数据: {}", metricExpress, JSONUtil.toJsonStr(params));
        }

        return (R) metricExpression.execute(params);
    }

}
