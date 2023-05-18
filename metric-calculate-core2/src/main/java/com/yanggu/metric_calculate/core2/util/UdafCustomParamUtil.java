package com.yanggu.metric_calculate.core2.util;


import cn.hutool.core.annotation.AggregateAnnotation;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.yanggu.metric_calculate.core2.annotation.UdafCustomParam;
import lombok.SneakyThrows;

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
            //如果是聚合量
            if (!ClassUtil.isBasicType(fieldClazz)) {
                //并且是MergedUnit子类, 递归查找子类是否包含自定义参数
                if (AggregateAnnotation.class.isAssignableFrom(fieldClazz)) {
                    List<UdafCustomParamData> tempList = getUdafCustomParamList(fieldClazz);
                    if (CollUtil.isNotEmpty(tempList)) {
                        udafCustomParamDataList.addAll(tempList);
                    }
                }
            } else {
                //如果是基本数据类型
                //判断是否包含该注解
                if (!field.isAnnotationPresent(UdafCustomParam.class)) {
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
                udafCustomParamData.setDefaultValue(field.get(clazz.newInstance()));
                //设置能否被更新
                udafCustomParamData.setUpdate(udafParam.update());
                //设置能够为空
                udafCustomParamData.setNotNull(udafParam.notNull());
                //设置描述
                udafCustomParamData.setDescription(udafParam.description());
                udafCustomParamDataList.add(udafCustomParamData);
            }
        }
        return udafCustomParamDataList;
    }

}
