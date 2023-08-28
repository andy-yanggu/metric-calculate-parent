package com.yanggu.metric_calculate.core.util;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.UdafCustomParam;
import lombok.SneakyThrows;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UdafCustomParamUtil {

    private UdafCustomParamUtil() {
    }

    @SneakyThrows
    public static List<UdafCustomParamData> getUdafCustomParamList(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<UdafCustomParamData> udafCustomParamDataList = new ArrayList<>();
        if (ArrayUtil.isEmpty(declaredFields)) {
            return udafCustomParamDataList;
        }
        for (Field field : declaredFields) {
            Class<?> fieldClazz = field.getType();
            //如果不是基本数据类型或者不包含该注解就continue
            if (!ClassUtil.isBasicType(fieldClazz) || !field.isAnnotationPresent(UdafCustomParam.class)) {
                continue;
            }
            UdafCustomParam udafParam = field.getAnnotation(UdafCustomParam.class);
            UdafCustomParamData udafCustomParamData = new UdafCustomParamData();
            //设置名称
            udafCustomParamData.setName(field.getName());
            //设置数据类型
            udafCustomParamData.setDataType(field.getType().getSimpleName());
            //设置默认值
            ReflectUtil.setAccessible(field);
            udafCustomParamData.setDefaultValue(field.get(clazz.getConstructor().newInstance()));
            //设置能否被更新
            udafCustomParamData.setUpdate(udafParam.update());
            //设置能够为空
            udafCustomParamData.setNotNull(udafParam.notNull());
            //设置描述
            udafCustomParamData.setDescription(udafParam.description());
            udafCustomParamDataList.add(udafCustomParamData);
        }
        return udafCustomParamDataList;
    }

}
