package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 聚合型处理器继承自度量字段处理器
 * <p>数值型、对象型、集合型继承该抽象类</p>
 */
@Data
@Slf4j
@NoArgsConstructor
public abstract class BaseAggregateFieldProcessor<T, M extends MergedUnit<M>> implements AggregateFieldProcessor<T, M> {

    protected BaseUdafParam udafParam;

    /**
     * 聚合类型
     */
    protected String aggregateType;

    protected Class<? extends MergedUnit<?>> mergeUnitClazz;

    /**
     * 用于生成MergeUnit
     */
    protected UnitFactory unitFactory;

    /**
     * 宽表字段
     */
    protected Map<String, Class<?>> fieldMap;

    @Override
    public void init() throws Exception {
        if (StrUtil.isBlank(aggregateType)) {
            throw new RuntimeException("聚合类型为空");
        }

        if (unitFactory == null) {
            throw new RuntimeException("UnitFactory为空");
        }

        if (mergeUnitClazz == null) {
            throw new RuntimeException("需要设置mergeUnitClazz");
        }
    }

    @Override
    @SneakyThrows
    public M process(T input) {
        throw new RuntimeException("子类需要重写process方法");
    }

    protected CloneWrapper<Object> getRetainFieldValue(T input, boolean retainObject) {
        Object value;
        if (retainObject) {
            value = input;
        } else {
            Object retainField = getRetainFieldValueFieldProcessor().process(input);
            if (retainField == null) {
                return null;
            }
            value = retainField;
        }
        return CloneWrapper.wrap(value);
    }

    protected MetricFieldProcessor<T, ?> getRetainFieldValueFieldProcessor() {
        throw new RuntimeException("需要重写getRetainFieldValueFieldProcessor方法");
    }

}
