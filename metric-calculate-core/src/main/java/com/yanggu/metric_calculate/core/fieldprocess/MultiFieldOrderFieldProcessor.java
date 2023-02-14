package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段排序字段处理器
 */
@Data
public class MultiFieldOrderFieldProcessor implements FieldProcessor<JSONObject, MultiFieldOrderCompareKey> {

    private Map<String, Class<?>> fieldMap;

    private List<FieldOrderParam> fieldOrderParamList;

    private List<Expression> expressionList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(fieldOrderParamList)) {
            throw new RuntimeException("排序字段为空");
        }
        this.expressionList = fieldOrderParamList.stream()
                .map(tempFieldOrderParam -> {
                    String metricExpress = tempFieldOrderParam.getExpress();
                    if (StrUtil.isBlank(metricExpress)) {
                        throw new RuntimeException("度量表达式为空");
                    }
                    AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
                    //编译度量字段表达式
                    Expression tempMetricExpression = instance.compile(metricExpress, true);
                    List<String> variableNames = tempMetricExpression.getVariableNames();
                    //检查数据明细宽表中是否包含当前参数
                    if (CollUtil.isNotEmpty(variableNames)) {
                        variableNames.forEach(tempName -> {
                            if (!fieldMap.containsKey(tempName)) {
                                throw new RuntimeException("数据明细宽表中没有该度量字段: " + tempName);
                            }
                        });
                    }
                    return tempMetricExpression;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MultiFieldOrderCompareKey process(JSONObject input) throws Exception {
        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);

        int size = fieldOrderParamList.size();
        List<FieldOrder> fieldOrderList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            FieldOrderParam fieldOrderParam = fieldOrderParamList.get(i);
            Expression expression = expressionList.get(i);
            Object execute = expression.execute(param);
            FieldOrder fieldOrder = new FieldOrder();
            fieldOrder.setResult(execute);
            fieldOrder.setDesc(fieldOrderParam.getDesc());
            fieldOrderList.add(fieldOrder);
        }
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setFieldOrderList(fieldOrderList);
        return multiFieldOrderCompareKey;
    }

}
