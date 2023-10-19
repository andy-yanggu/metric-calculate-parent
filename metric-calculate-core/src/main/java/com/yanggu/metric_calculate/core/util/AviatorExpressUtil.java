package com.yanggu.metric_calculate.core.util;


import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import com.yanggu.metric_calculate.core.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorFunctionInstance;
import org.dromara.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.aviator.Options.USE_USER_ENV_AS_TOP_ENV_DIRECTLY;

/**
 * Aviator表达式工具类
 */
public class AviatorExpressUtil {

    private AviatorExpressUtil() {
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
        AviatorEvaluatorInstance aviatorEvaluatorInstance;
        if (CollUtil.isEmpty(aviatorExpressParam.getAviatorFunctionInstanceList())) {
            //默认使用全局单例的AviatorEvaluator
            aviatorEvaluatorInstance = AviatorEvaluator.getInstance();
        } else {
            //如果有自定义函数, 使用单独的AviatorEvaluator
            //例如多个同名的自定义函数, 但是参数不同, 是无法区分的, 所以使用单独的实例
            aviatorEvaluatorInstance = AviatorEvaluator.newInstance();
            //设置自定义Aviator函数
            for (AviatorFunctionInstance aviatorFunctionInstance : aviatorExpressParam.getAviatorFunctionInstanceList()) {
                String name = aviatorFunctionInstance.getName();
                Map<String, Object> param = aviatorFunctionInstance.getParam();
                AbstractUdfAviatorFunction aviatorFunction = aviatorFunctionFactory.initAviatorFunction(name, param);
                aviatorEvaluatorInstance.addFunction(name, aviatorFunction);
            }
        }
        //设置Java反射调用
        aviatorEvaluatorInstance.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
        //设置不使用客户传入的env
        aviatorEvaluatorInstance.setOption(USE_USER_ENV_AS_TOP_ENV_DIRECTLY, false);
        try {
            //编译表达式
            return aviatorEvaluatorInstance.compile(aviatorExpressParam.getExpress(), true);
        } catch (Exception e) {
            throw new RuntimeException("Aviator表达式编译失败, 请检查表达式编写是否正确或者传参是否正确");
        }
    }

    /**
     * 验证依赖参数是否正确
     *
     * @param expression
     * @param paramSet
     */
    public static void checkVariable(Expression expression, Set<String> paramSet) {
        List<String> variableNames = expression.getVariableNames();
        if (CollUtil.isEmpty(variableNames)) {
            return;
        }
        //验证数据明细宽表中是否包含该字段
        variableNames.forEach(tempName -> {
            if (!paramSet.contains(tempName)) {
                throw new RuntimeException("数据明细宽表中没有该字段: " + tempName);
            }
        });
    }

}
