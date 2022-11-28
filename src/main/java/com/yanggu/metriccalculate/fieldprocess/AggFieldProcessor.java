package com.yanggu.metriccalculate.fieldprocess;

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

import java.util.Map;

/**
 * 聚合字段处理器
 */
@Data
@Slf4j
public class AggFieldProcessor implements FieldExtractProcessor<JSONObject, MergedUnit<?>> {

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
        metricExpression = instance.compile(metricExpress, true);
    }

    @Override
    public MergedUnit<?> process(JSONObject input) throws Exception {
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);
        if (log.isDebugEnabled()) {
            log.debug("度量字段表达式: {}, 输入的数据: {}", metricExpress, JSONUtil.toJsonStr(params));
        }

        Object execute = metricExpression.execute(params);

        return UnitFactory.initInstanceByValue(aggregateType, execute);
    }

}
