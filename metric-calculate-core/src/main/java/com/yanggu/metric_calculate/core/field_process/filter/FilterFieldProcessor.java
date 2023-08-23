package com.yanggu.metric_calculate.core.field_process.filter;

import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.util.ExpressionUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 前置过滤表达式处理器, 输入明细数据, 执行表达式, 返回Boolean
 */
@Slf4j
@Getter
@EqualsAndHashCode
public class FilterFieldProcessor implements FieldProcessor<JSONObject, Boolean> {

    /**
     * 宽表字段
     */
    private final Map<String, Class<?>> fieldMap;

    /**
     * 前置过滤条件表达式
     */
    private final AviatorExpressParam filterExpressParam;

    /**
     * Aviator函数工厂类
     */
    private final AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 前置过滤表达式
     */
    private Expression filterExpression;

    public FilterFieldProcessor(Map<String, Class<?>> fieldMap,
                                AviatorExpressParam filterExpressParam,
                                AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.filterExpressParam = filterExpressParam;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    /**
     * 编译前置过滤表达式
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        //如果前置为空, 直接return
        if (filterExpressParam == null || StrUtil.isBlank(filterExpressParam.getExpress())) {
            return;
        }
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }
        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        //编译表达式
        Expression tempFilterExpression = ExpressionUtil.compileExpress(filterExpressParam, aviatorFunctionFactory);
        List<String> variableNames = tempFilterExpression.getVariableNames();
        if (CollUtil.isEmpty(variableNames)) {
            throw new RuntimeException("过滤条件为常量表达式, 没有意义: " + filterExpressParam.getExpress());
        }
        //验证数据明细宽表中是否包含该字段
        ExpressionUtil.checkVariable(tempFilterExpression, fieldMap.keySet());
        this.filterExpression = tempFilterExpression;
    }

    @Override
    public Boolean process(JSONObject input) {
        //如果表达式为空, 直接return true
        if (filterExpressParam == null || StrUtil.isBlank(filterExpressParam.getExpress())) {
            return true;
        }
        //执行过滤表达式
        return (boolean) filterExpression.execute(input);
    }

}

