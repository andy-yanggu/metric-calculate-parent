package com.yanggu.metriccalculate.fieldprocess;

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
        //如果前置为空, 直接return true
        if (StrUtil.isBlank(filterExpress)) {
            filterExpress = "true";
        }
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        filterExpression = instance.compile(filterExpress, true);
    }

    @Override
    public Boolean process(JSONObject input) throws Exception {
        //获取执行参数
        Map<String, Object> params = MetricUtil.getParam(input, fieldMap);
        if (log.isDebugEnabled()) {
            log.debug("前置过滤条件: {}, 输入的数据: {}", filterExpress, JSONUtil.toJsonStr(params));
        }

        return ((boolean) filterExpression.execute(params));
    }

}
