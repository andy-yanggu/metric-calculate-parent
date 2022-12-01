package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metriccalculate.util.MetricUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 前置过滤表达式处理器
 */
@Data
@Slf4j
@NoArgsConstructor
public class FilterProcessor implements FieldExtractProcessor<JSONObject, Boolean> {

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 前置过滤条件表达式
     */
    private String filterExpress;

    /**
     * 前置过滤表达式
     */
    private Expression filterExpression;

    public FilterProcessor(Map<String, Class<?>> fieldMap, String filterExpress) {
        this.fieldMap = fieldMap;
        this.filterExpress = filterExpress;
    }

    //编译前置过滤表达式
    @Override
    public void init() throws Exception {
        //如果前置为空, 直接return
        if (StrUtil.isBlank(filterExpress)) {
            return;
        }
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();

        Expression tempFilterExpression = instance.compile(filterExpress, true);
        List<String> variableNames = tempFilterExpression.getVariableNames();
        if (CollUtil.isEmpty(variableNames)) {
            throw new RuntimeException("过滤条件为常量表达式, 没有意义: " + filterExpress);
        }
        //验证数据明细宽表中是否包含该字段
        variableNames.forEach(tempName -> {
            if (!fieldMap.containsKey(tempName)) {
                throw new RuntimeException("数据明细宽表中没有该字段: " + tempName);
            }
        });

        filterExpression = tempFilterExpression;
    }

    @Override
    public Boolean process(JSONObject input) {
        //如果表达式为空, 直接return true
        if (StrUtil.isBlank(filterExpress)) {
            return true;
        }
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);
        if (log.isDebugEnabled()) {
            log.debug("前置过滤条件: {}, 输入的数据: {}", filterExpress, JSONUtil.toJsonStr(params));
        }

        return ((boolean) filterExpression.execute(params));
    }

}

