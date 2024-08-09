package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

public class Sum_${class_name}_AggregateFunction implements AggregateFunction<${class_name}, ${class_name}, ${class_name}> {

@Override
public ${class_name} createAccumulator() {
<#if class_name == 'Integer'>
    return 0;
<#elseif class_name == 'Long'>
    return 0L;
<#elseif class_name == 'Float'>
    return 0.0F;
<#elseif class_name == 'Double'>
    return 0.0D;
<#else>
    throw new RuntimeException("类型错误" + ${class_name});
</#if>
}

@Override
public ${class_name} add(Object value, Object accumulator) {
return ((${class_name}) accumulator) + ((${class_name}) value);
}

@Override
public ${class_name} getResult(Object accumulator) {
return (${class_name}) accumulator;
}

@Override
public Object merge(Object a, Object b) {
return ((${class_name}) a) + ((${class_name}) b);
}

}
