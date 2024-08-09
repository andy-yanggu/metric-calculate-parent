package com.yanggu.metric_calculate.config.util.excel;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class Dto2ImportErrDtoUtil {

    /**
     * .
     *
     * @param source source
     * @param target target
     */
    public static void copyProperties(Object source, Object target) throws IllegalAccessException,
            InstantiationException {
        Class sourceClazz = source.getClass();
        Class targetClazz = target.getClass();
        Field[] sourceFields = sourceClazz.getDeclaredFields();
        Field[] targetFields = targetClazz.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            for (Field targetField : targetFields) {
                if (sourceField.getName().equals(targetField.getName())) {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);
                    if (targetField.getType() == ImportErrDto.class) {
                        ImportErrDto importErrDto = new ImportErrDto();
                        importErrDto.setValue(sourceField.get(source));
                        targetField.set(target, importErrDto);
                        break;
                    } else if (sourceField.getType() == List.class && targetField.getType() == List.class) {
                        Class targetListColumnClazz = (Class) ((ParameterizedType) targetField.getGenericType())
                                .getActualTypeArguments()[0];
                        List sourceListColumn = (List) sourceField.get(source);
                        List targetListColumn = null;
                        if (sourceListColumn != null && !sourceListColumn.isEmpty()) {
                            targetListColumn = new ArrayList(sourceListColumn.size());
                            for (Object object1 : sourceListColumn) {
                                Object object2 = targetListColumnClazz.newInstance();
                                copyProperties2(object1, object2);
                                targetListColumn.add(object2);
                            }
                        }
                        targetField.set(target, targetListColumn);
                    }
                }
            }
        }
    }

    private static void copyProperties2(Object source, Object target) throws IllegalAccessException {
        Class sourceClazz = source.getClass();
        Class targetClazz = target.getClass();
        Field[] sourceFields = sourceClazz.getDeclaredFields();
        Field[] targetFields = targetClazz.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            for (Field targetField : targetFields) {
                if (sourceField.getName().equals(targetField.getName())) {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);
                    if (targetField.getType() == ImportErrDto.class) {
                        ImportErrDto importErrDto = new ImportErrDto();
                        importErrDto.setValue(sourceField.get(source));
                        targetField.set(target, importErrDto);
                        break;
                    }
                }
            }
        }
    }
}
