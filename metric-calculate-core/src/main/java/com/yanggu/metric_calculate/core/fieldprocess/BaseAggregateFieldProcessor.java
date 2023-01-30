package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 聚合型处理器继承自度量字段处理器
 * <p>数值型、对象型、集合型继承该抽象类</p>
 */
@Data
@Slf4j
@NoArgsConstructor
public abstract class BaseAggregateFieldProcessor<M extends MergedUnit<M>> extends MetricFieldProcessor<Object> {

    /**
     * 聚合类型
     */
    protected String aggregateType;

    protected Class<? extends MergedUnit<?>> mergeUnitClazz;

    /**
     * 是否是自定义udaf
     */
    protected Boolean isUdaf;

    /**
     * 用户自定义聚合函数的参数
     */
    protected Map<String, Object> udafParams;

    /**
     * 用于生成MergeUnit
     */
    protected UnitFactory unitFactory;

    @Override
    public void init() throws Exception {
        //初始化度量字段表达式
        super.init();

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

    /**
     * @param useCompareField 是否使用比较字段
     * @param retainObject    是否保留对象
     * @param input           输入明细数据
     * @return
     */
    protected M getResult(boolean useCompareField, boolean retainObject, JSONObject input) throws Exception {
        //获取执行参数
        Object result;
        if (useCompareField) {
            //获取比较值
            Object compareFieldValue = super.process(input);
            if (compareFieldValue == null) {
                return null;
            }

            //获取保留值
            Object value = getValue(input, retainObject);
            if (value == null) {
                return null;
            }
            result = new KeyValue<>((Comparable<?>) compareFieldValue, value);
        } else {
            //没有比较字段, 直接获取保留值
            Object value = getValue(input, retainObject);
            if (value == null) {
                return null;
            }
            result = Cloneable2Wrapper.wrap(value);
        }
        return (M) unitFactory.initInstanceByValue(aggregateType, result, udafParams);
    }

    private Object getValue(JSONObject input, boolean retainObject) throws Exception {
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
        return value;
    }

    protected MetricFieldProcessor<?> getRetainFieldValueFieldProcessor() {
        throw new RuntimeException("需要重写getRetainFieldValueFieldProcessor方法");
    }

}
