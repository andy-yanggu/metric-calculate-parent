package com.yanggu.metric_calculate.core.unit;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.client.magiccube.enums.BasicType;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import com.yanggu.metric_calculate.core.unit.obj.ObjectiveUnit;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.client.magiccube.enums.BasicType.*;

public class UnitFactory {

    private Map<String, Class<? extends MergedUnit>> methodReflection = new HashMap<>();

    public UnitFactory() throws Exception {
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class);
        Set<Class<?>> classSet = ClassUtil.scanPackage("com.yanggu.metric_calculate.core.unit", classFilter);
        classSet.forEach(tempClazz -> {
            MergeType annotation = tempClazz.getAnnotation(MergeType.class);
            methodReflection.put(annotation.value(), (Class<? extends MergedUnit>) tempClazz);
        });

        //TODO 以后支持添加自定义的聚合函数
        String pathname = "D:\\project\\self\\metric-calculate\\udf-test\\target\\udf-test-1.0.0-SNAPSHOT.jar";
        File file = new File(pathname);
        Enumeration<JarEntry> entries = new JarFile(file).entries();
        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, ClassLoader.getSystemClassLoader());
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                String entryName = entry.getName().substring(0, entry.getName().indexOf(".class")).replaceAll("/", ".");
                Class<?> loadClass = urlClassLoader.loadClass(entryName);
                Annotation[] annotations = loadClass.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (MergeType.class.getName().equals(annotation.annotationType().getName())) {
                        //@com.yanggu.metric_calculate.core.annotation.MergeType(value=MYUNIT)
                        String annotationString = annotation.toString();
                        String between = StrUtil.subBetween(annotationString, "(", ")");
                        String[] split = between.split(", ");
                        Map<String, String> collect = Arrays.stream(split)
                                .collect(Collectors.toMap(s -> s.split("=")[0], s -> s.split("=")[1]));
                        String value = collect.get("value");
                        methodReflection.put(value, (Class<? extends MergedUnit>) loadClass);
                        break;
                    }
                }
            }
        }
    }

    public Class<? extends MergedUnit> getMergeableClass(String actionType) {
        return methodReflection.get(actionType);
    }

    /**
     * Get unit instance by init value.
     */
    public static MergedUnit initInstanceByValue(String mergeable, Object initValue) throws Exception {
        Class clazz = new UnitFactory().getMergeableClass(mergeable);
        if (clazz == null) {
            throw new NullPointerException("MergedUnit class not found.");
        }
        List<String> collect = Arrays.stream(clazz.getAnnotations()).map(temp -> temp.annotationType().getSimpleName()).collect(Collectors.toList());
        if (collect.contains(Numerical.class.getSimpleName())) {
            return createNumericUnit(clazz, initValue);
        } else if (collect.contains(Collective.class.getSimpleName())) {
            return createCollectiveUnit(clazz, initValue);
        } else if (collect.contains(Objective.class.getSimpleName())) {
            return createObjectiveUnit(clazz, initValue);
        }
        throw new RuntimeException(clazz.getName() + " not support.");
    }

    /**
     * Create unit.
     */
    public static MergedUnit createObjectiveUnit(Class<ObjectiveUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().value(initValue);
    }

    /**
     * Create collective unit.
     */
    public static MergedUnit createCollectiveUnit(Class<CollectionUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().add(initValue);
    }

    /**
     * Create number unit.
     */
    public static NumberUnit createNumericUnit(Class<NumberUnit> clazz, Object initValue) throws Exception {
        Constructor<NumberUnit> constructor = clazz.getConstructor(CubeNumber.class);
        BasicType valueType = ofValue(initValue);
        switch (valueType) {
            case LONG:
                return constructor.newInstance(CubeLong.of((Number) initValue));
            case DECIMAL:
                return constructor.newInstance(CubeDecimal.of((Number) initValue));
            default:
                throw new IllegalStateException("Unexpected value type: " + valueType);
        }
    }

    public static BasicType ofValue(Object value) {
        if (value instanceof Long) {
            return LONG;
        }
        if (value instanceof String) {
            return STRING;
        }
        if (value instanceof Boolean) {
            return BOOLEAN;
        }
        if (value instanceof BigDecimal) {
            return DECIMAL;
        }
        throw new IllegalArgumentException(String.format("Not support type: %s", value.getClass().getName()));
    }

    public static void main(String[] args) throws Exception {
        MergedUnit count2 = initInstanceByValue("COUNT2", 1L);
        System.out.println(count2);
    }

}
