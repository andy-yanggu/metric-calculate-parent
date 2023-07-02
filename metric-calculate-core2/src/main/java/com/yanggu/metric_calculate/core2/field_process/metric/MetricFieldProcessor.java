package com.yanggu.metric_calculate.core2.field_process.metric;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import com.yanggu.metric_calculate.core2.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.UdfAviatorFunctionParam;
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
public class MetricFieldProcessor<R> implements FieldProcessor<JSONObject, R> {

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 表达式配置
     */
    private AviatorExpressParam aviatorExpressParam;

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

        AviatorEvaluatorInstance aviatorEvaluatorInstance = AviatorEvaluator.getInstance();
        //是否使用自定义Aviator函数
        if (Boolean.TRUE.equals(aviatorExpressParam.getUseUdfFunction())
                && CollUtil.isNotEmpty(aviatorExpressParam.getUdfAviatorFunctionParamList())) {
            aviatorEvaluatorInstance = AviatorEvaluator.newInstance();
            //设置自定义Aviator函数
            for (UdfAviatorFunctionParam udfAviatorFunctionParam : aviatorExpressParam.getUdfAviatorFunctionParamList()) {
                String name = udfAviatorFunctionParam.getName();
                AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.getAviatorFunction(name);
                AviatorFunctionFactory.setUdfParam(aviatorFunction, udfAviatorFunctionParam.getParam());
                aviatorFunction.init();
                aviatorEvaluatorInstance.addFunction(name, aviatorFunction);
            }
        }

        aviatorEvaluatorInstance.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
        Expression tempMetricExpression = aviatorEvaluatorInstance.compile(aviatorExpressParam.getExpress(), true);
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
    public R process(JSONObject input) {
        return (R) metricExpression.execute(input);
    }

}
