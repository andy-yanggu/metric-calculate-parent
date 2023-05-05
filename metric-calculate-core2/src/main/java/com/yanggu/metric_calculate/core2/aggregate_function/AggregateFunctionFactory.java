package com.yanggu.metric_calculate.core2.aggregate_function;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class AggregateFunctionFactory {

    /**
     * 内置AggregateFunction的包路径
     */
    public static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core2.aggregate_function";

    private static final String ERROR_MESSAGE = "自定义聚合函数唯一标识重复, 重复的全类名: ";

    /**
     * 内置的AggregateFunction
     */
    private static final Map<String, Class<? extends AggregateFunction>> BUILT_IN_FUNCTION_MAP = new HashMap<>();

    private Map<String, Class<? extends AggregateFunction>> functionMap = new HashMap<>();

    /**
     * udaf的jar包路径
     */
    private List<String> udafJarPathList;

    static {
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class)
                && AggregateFunction.class.isAssignableFrom(clazz);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, classFilter);
        for (Class<?> tempClazz : classSet) {
            MergeType annotation = tempClazz.getAnnotation(MergeType.class);
            String value = annotation.value();
            Class<? extends AggregateFunction> put = BUILT_IN_FUNCTION_MAP.put(value, (Class<? extends AggregateFunction>) tempClazz);
            if (put != null) {
                throw new RuntimeException(ERROR_MESSAGE + put.getName());
            }
        }
    }

    public AggregateFunctionFactory(List<String> udafJarPathList) {
        this.udafJarPathList = udafJarPathList;
    }

    /**
     * 添加系统自带的聚合函数和用户自定义的聚合函数
     *
     * @throws Exception
     */
    public void init() {
        //放入内置的BUILT_IN_UNIT_MAP
        BUILT_IN_FUNCTION_MAP.forEach((aggregateType, clazz) -> {
            Class<? extends AggregateFunction> put = functionMap.put(aggregateType, clazz);
            if (put != null) {
                throw new RuntimeException(ERROR_MESSAGE + put.getName());
            }
        });

        if (CollUtil.isEmpty(udafJarPathList)) {
            return;
        }
    }

    /**
     * 通过反射给聚合函数设置参数
     *
     * @param aggregateFunction
     * @param params
     */
    public static <IN, ACC, OUT> void setUdafParam(AggregateFunction<IN, ACC, OUT> aggregateFunction, Map<String, Object> params) {
        Field[] declaredFields = aggregateFunction.getClass().getDeclaredFields();
        //通过反射给聚合函数的参数赋值
        if (CollUtil.isNotEmpty(params) && ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                Object fieldData = params.get(field.getName());
                if (fieldData != null) {
                    //通过反射给字段赋值
                    ReflectUtil.setFieldValue(aggregateFunction, field, fieldData);
                }
            }
        }
        aggregateFunction.init();
    }

    /**
     * 通过反射使用空参构造创建聚合函数
     *
     * @param aggregate
     * @return
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     */
    @SneakyThrows
    public <IN, ACC, OUT> AggregateFunction<IN, ACC, OUT> getAggregateFunction(String aggregate) {
        Class<? extends AggregateFunction> clazz = functionMap.get(aggregate);
        if (clazz == null) {
            throw new RuntimeException("传入的" + aggregate + "有误");
        }
        return clazz.newInstance();
    }

}
