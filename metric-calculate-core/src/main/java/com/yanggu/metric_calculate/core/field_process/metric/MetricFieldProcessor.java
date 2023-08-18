package com.yanggu.metric_calculate.core.field_process.metric;

import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.util.ExpressionUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.Map;

/**
 * 度量值字段处理器, 从输入的明细数据中，提取出度量值
 */
@Data
@Slf4j
@NoArgsConstructor
public class MetricFieldProcessor<R> implements FieldProcessor<JSONObject, R> {

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 表达式配置
     */
    private AviatorExpressParam aviatorExpressParam;

    /**
     * Aviator函数工厂类
     */
    private AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 度量字段表达式
     */
    private Expression metricExpression;

    @Override
    public void init() throws Exception {
        if (aviatorExpressParam == null || StrUtil.isBlank(aviatorExpressParam.getExpress())) {
            throw new RuntimeException("Aviator表达式配置为空");
        }
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }
        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }
        //编译表达式
        Expression tempMetricExpression = ExpressionUtil.compileExpress(aviatorExpressParam, aviatorFunctionFactory);
        //验证数据明细宽表中是否包含该字段
        ExpressionUtil.checkVariable(tempMetricExpression, fieldMap);

        this.metricExpression = tempMetricExpression;
    }

    @Override
    @SneakyThrows
    public R process(JSONObject input) {
        return (R) metricExpression.execute(input);
    }

}
