package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

//TODO  增加kryo序列化器

/**
 * 聚合集合型字段处理器
 *
 * @param <M>
 */
@Data
public class AggregateCollectionFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    private MetricFieldProcessor<?> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {

        //如果是设置了比较字段
        Collective objective = mergeUnitClazz.getAnnotation(Collective.class);
        if (objective.useCompareField()) {
            super.init();
        }

        //如果设置了保留字段
        if (!objective.retainObject() && retainFieldValueFieldProcessor == null) {
            throw new RuntimeException("保留字段处理器为空");
        }
    }

    @Override
    public M process(JSONObject input) throws Exception {

        Collective objective = mergeUnitClazz.getAnnotation(Collective.class);

        //是否使用比较字段
        boolean useCompareField = objective.useCompareField();

        //是否保留对象
        boolean retainObject = objective.retainObject();

        //使用比较字段
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
        Map<String, Object> test = test(udafParams);
        return (M) unitFactory.initInstanceByValue(aggregateType, result, test);
    }

    private Object getValue(JSONObject input, boolean retainObject) throws Exception {
        Object value;
        if (retainObject) {
            value = input;
        } else {
            Object retainField = retainFieldValueFieldProcessor.process(input);
            if (retainField == null) {
                return null;
            }
            value = retainField;
        }
        return value;
    }

    @SneakyThrows
    private Map<String, Object> test(Map<String, Object> udafParams) {
        if (CollUtil.isEmpty(udafParams)) {
            return udafParams;
        }

        //如果是计数窗口, 需要添加聚合字段处理器
        MergeType annotation = mergeUnitClazz.getAnnotation(MergeType.class);
        if (!annotation.countWindow()) {
            return udafParams;
        }

        //滑动计数窗口的udafParams参数
        /*
        {
        	"limit": 5, //滑动计数窗口大小
        	"aggregateType": "SUM", //聚合类型
        	"udafParams": { //自定义udaf参数
        		"metricExpress": "amount", //度量字段(数值)、比较字段(排序或者去重) TODO 需要前端手动设置原子指标度量字段名
        		"retainExpress": "", //保留字段名
        	}
        }
        */

        //如果是一个普通的sortlistfield
        /*
        {
          "desc": true, //升序还是降序
          "limit": 10,  //限制大小
          "retainExpress": "amount" //保留字段名
        }
        */

        Object aggregateType = udafParams.get("aggregateType");
        if (StrUtil.isBlankIfStr(aggregateType)) {
            throw new RuntimeException("滑动计数窗口需要设置聚合类型aggregateType");
        }

        Object subUdafParamsMapObject = udafParams.get("udafParams");
        Map<String, Object> subUdafParams = new HashMap<>();
        Object metricExpress = null;
        if (subUdafParamsMapObject instanceof Map && CollUtil.isNotEmpty((Map<?, ?>) subUdafParamsMapObject)) {
            subUdafParams = (Map<String, Object>) subUdafParamsMapObject;
            metricExpress = subUdafParams.get("metricExpress");
        }

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor =
                MetricUtil.getAggregateFieldProcessor(unitFactory, subUdafParams, getFieldMap(), metricExpress, aggregateType.toString());

        aggregateFieldProcessor.setUdafParams(subUdafParams);

        Map<String, Object> returnMap = new HashMap<>(udafParams);
        returnMap.put("aggregateFieldProcessor", aggregateFieldProcessor);
        return returnMap;
    }

}
