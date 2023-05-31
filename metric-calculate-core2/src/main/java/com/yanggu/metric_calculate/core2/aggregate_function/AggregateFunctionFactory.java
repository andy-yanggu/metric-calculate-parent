package com.yanggu.metric_calculate.core2.aggregate_function;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 聚合函数工厂类
 */
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
            //添加到内置的map中
            addClassToMap(tempClazz, BUILT_IN_FUNCTION_MAP);
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
    public void init() throws Exception {
        //放入内置的BUILT_IN_UNIT_MAP
        functionMap.putAll(BUILT_IN_FUNCTION_MAP);

        //自定义的udaf的jar路径
        if (CollUtil.isEmpty(udafJarPathList)) {
            return;
        }

        //支持添加自定义的聚合函数
        URL[] urls = new URL[udafJarPathList.size()];
        List<JarEntry> jarEntries = new ArrayList<>();
        for (int i = 0; i < udafJarPathList.size(); i++) {
            String udafJarPath = udafJarPathList.get(i);
            File file = new File(udafJarPath);
            urls[i] = file.toURI().toURL();

            JarFile jarFile = new JarFile(udafJarPath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                jarEntries.add(entries.nextElement());
            }
        }

        //这里父类指定为系统类加载器, 子类加载可以访问父类加载器中加载的类,
        //但是父类不可以访问子类加载器中加载的类, 线程上下文类加载器除外
        try (URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader())) {
            //扫描有MergeType注解
            Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class);
            for (JarEntry entry : jarEntries) {
                if (entry.isDirectory() || !entry.getName().endsWith(".class") || entry.getName().contains("$")) {
                    continue;
                }
                String entryName = entry.getName()
                        .substring(0, entry.getName().indexOf(".class"))
                        .replace("/", ".");
                Class<?> loadClass = urlClassLoader.loadClass(entryName);
                if (classFilter.accept(loadClass)) {
                    //添加到map中
                    addClassToMap(loadClass, functionMap);
                }
            }
        }
    }

    /**
     * 通过反射给聚合函数设置参数
     *
     * @param aggregateFunction
     * @param params
     */
    public static <IN, ACC, OUT> void setUdafParam(AggregateFunction<IN, ACC, OUT> aggregateFunction,
                                                   Map<String, Object> params) {
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
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     * @return
     */
    @SneakyThrows
    public <IN, ACC, OUT> AggregateFunction<IN, ACC, OUT> getAggregateFunction(String aggregate) {
        Class<? extends AggregateFunction> clazz = getAggregateFunctionClass(aggregate);
        return clazz.newInstance();
    }

    public Class<? extends AggregateFunction> getAggregateFunctionClass(String aggregate) {
        if (StrUtil.isBlank(aggregate)) {
            throw new RuntimeException("传入的聚合类型为空");
        }
        Class<? extends AggregateFunction> clazz = functionMap.get(aggregate);
        if (clazz == null) {
            throw new RuntimeException("传入的" + aggregate + "有误");
        }
        return clazz;
    }

    private static void addClassToMap(Class<?> tempClazz, Map<String, Class<? extends AggregateFunction>> functionMap) {
        MergeType annotation = tempClazz.getAnnotation(MergeType.class);
        if (annotation == null) {
            return;
        }
        String value = annotation.value();
        Class<? extends AggregateFunction> put = functionMap.put(value, (Class<? extends AggregateFunction>) tempClazz);
        if (put != null) {
            throw new RuntimeException(ERROR_MESSAGE + put.getName());
        }
    }

}
