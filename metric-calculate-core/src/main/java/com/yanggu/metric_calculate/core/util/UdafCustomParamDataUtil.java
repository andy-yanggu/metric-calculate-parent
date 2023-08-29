package com.yanggu.metric_calculate.core.util;


import lombok.SneakyThrows;
import org.dromara.hutool.core.annotation.AnnotationUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ReflectUtil;
import org.dromara.hutool.json.JSONUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UdafCustomParamDataUtil {

    private UdafCustomParamDataUtil() {
    }

    @SneakyThrows
    public static <T extends Annotation> List<UdafCustomParamData> getUdafCustomParamList(Class<?> clazz, Class<T> annotationClass) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<UdafCustomParamData> udafCustomParamDataList = new ArrayList<>();
        if (ArrayUtil.isEmpty(declaredFields)) {
            return udafCustomParamDataList;
        }
        for (Field field : declaredFields) {
            Class<?> fieldClazz = field.getType();
            //如果不是基本数据类型或者不包含该注解就continue
            if (!ClassUtil.isBasicType(fieldClazz) || !field.isAnnotationPresent(annotationClass)) {
                continue;
            }
            //获取注解数据
            Map<String, Object> annotationValueMap = AnnotationUtil.getAnnotationValueMap(field, annotationClass);
            UdafCustomParamData udafCustomParamData = JSONUtil.toBean(annotationValueMap, UdafCustomParamData.class);
            //设置名称
            udafCustomParamData.setName(field.getName());
            //设置数据类型
            udafCustomParamData.setDataType(field.getType().getSimpleName());
            //设置默认值
            ReflectUtil.setAccessible(field);
            udafCustomParamData.setDefaultValue(field.get(clazz.getConstructor().newInstance()));
            udafCustomParamDataList.add(udafCustomParamData);
        }
        return udafCustomParamDataList;
    }

}
