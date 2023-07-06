package com.yanggu.metric_calculate.core2.util;


import cn.hutool.core.collection.CollUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import com.yanggu.metric_calculate.core2.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorFunctionInstance;

import java.util.List;
import java.util.Map;

/**
 * Aviator表达式工具类
 */
public class ExpressionUtil {

    private ExpressionUtil() {
    }

    /**
     * 根据配置编译得到表达式
     *
     * @param aviatorExpressParam
     * @param aviatorFunctionFactory
     * @return
     */
    public static Expression compileExpress(AviatorExpressParam aviatorExpressParam,
                                            AviatorFunctionFactory aviatorFunctionFactory) {
        //默认使用全局单例的AviatorEvaluator
        AviatorEvaluatorInstance aviatorEvaluatorInstance = AviatorEvaluator.getInstance();
        List<AviatorFunctionInstance> aviatorFunctionInstanceList = aviatorExpressParam.getAviatorFunctionInstanceList();
        //是否使用自定义Aviator函数
        if (CollUtil.isNotEmpty(aviatorFunctionInstanceList)) {
            //如果有自定义函数, 使用单独的AviatorEvaluator
            //例如多个同名的自定义函数, 但是参数不同, 是无法区分的
            aviatorEvaluatorInstance = AviatorEvaluator.newInstance();
            //设置自定义Aviator函数
            for (AviatorFunctionInstance aviatorFunctionInstance : aviatorFunctionInstanceList) {
                String name = aviatorFunctionInstance.getName();
                AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.getAviatorFunction(name);
                AviatorFunctionFactory.init(aviatorFunction, aviatorFunctionInstance.getParam());
                aviatorEvaluatorInstance.addFunction(name, aviatorFunction);
            }
        }
        //设置Java反射调用
        aviatorEvaluatorInstance.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
        return aviatorEvaluatorInstance.compile(aviatorExpressParam.getExpress(), true);
    }

    public static void checkVariable(Expression expression, Map<String, Class<?>> fieldMap) {
        List<String> variableNames = expression.getVariableNames();
        if (CollUtil.isEmpty(variableNames)) {
            return;
        }
        //验证数据明细宽表中是否包含该字段
        variableNames.forEach(tempName -> {
            if (!fieldMap.containsKey(tempName)) {
                throw new RuntimeException("数据明细宽表中没有该字段: " + tempName);
            }
        });
    }

}
