package com.yanggu.metric_calculate.core2.field_process.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.util.ExpressionUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 前置过滤表达式处理器, 输入明细数据, 执行表达式, 返回Boolean
 */
@Data
@Slf4j
@NoArgsConstructor
public class FilterFieldProcessor implements FieldProcessor<JSONObject, Boolean> {

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 前置过滤条件表达式
     */
    private AviatorExpressParam filterExpressParam;

    /**
     * Aviator函数工厂类
     */
    private AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 前置过滤表达式
     */
    private Expression filterExpression;

    public FilterFieldProcessor(Map<String, Class<?>> fieldMap, AviatorExpressParam filterExpressParam) {
        this.fieldMap = fieldMap;
        this.filterExpressParam = filterExpressParam;
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
        ExpressionUtil.checkVariable(tempFilterExpression, fieldMap);
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

