package com.yanggu.metric_calculate.core.function_factory;


import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.FieldUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 函数工厂类
 */
public class FunctionFactory {

    private FunctionFactory() {
    }

    /**
     * 反射给对象赋值
     *
     * @param function
     * @param params
     */
    public static void setParam(Object function,
                                Map<String, Object> params) {
        if (function == null || MapUtil.isEmpty(params)) {
            return;
        }
        Field[] declaredFields = FieldUtil.getFields(function.getClass());
        //通过反射给聚合函数的参数赋值
        if (ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                Object fieldData = params.get(field.getName());
                if (fieldData != null) {
                    //通过反射给字段赋值
                    FieldUtil.setFieldValue(function, field, fieldData);
                }
            }
        }
    }

    /**
     * 创建URLClassLoader
     *
     * @param jarPathList
     * @return
     * @throws Exception
     */
    public static URLClassLoader buildURLClassLoader(List<String> jarPathList) throws Exception {
        if (CollUtil.isEmpty(jarPathList)) {
            throw new RuntimeException("jarPathList is empty");
        }
        URL[] urls = new URL[jarPathList.size()];
        for (int i = 0; i < jarPathList.size(); i++) {
            String jarPath = jarPathList.get(i);
            File file = new File(jarPath);
            urls[i] = file.toURI().toURL();
        }
        return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    /**
     * 从jar包中过滤出对应的class, 并执行相应的消费逻辑
     *
     * @param jarPathList
     * @param classFilter
     * @param consumer
     * @throws Exception
     */
    public static void loadClassFromJar(List<String> jarPathList,
                                        Predicate<Class<?>> classFilter,
                                        Consumer<Class<?>> consumer) throws Exception {
        URLClassLoader urlClassLoader = buildURLClassLoader(jarPathList);
        loadClassFromJar(urlClassLoader, jarPathList, classFilter, consumer);
    }

    /**
     * 从jar包中过滤出对应的class, 并执行相应的消费逻辑
     * <p>区别在于类加载器自己手动传入</p>
     *
     * @param jarPathList
     * @param classFilter
     * @param consumer
     * @throws Exception
     */
    public static void loadClassFromJar(ClassLoader classLoader,
                                        List<String> jarPathList,
                                        Predicate<Class<?>> classFilter,
                                        Consumer<Class<?>> consumer) throws Exception {
        List<String> fullNameList = getClassFullNameListFromJar(jarPathList);
        for (String classFullName : fullNameList) {
            Class<?> clazz = classLoader.loadClass(classFullName);
            if (classFilter.test(clazz)) {
                consumer.accept(clazz);
            }
        }
    }

    private static List<String> getClassFullNameListFromJar(List<String> jarPathList) throws Exception {
        List<String> classFullNameList = new ArrayList<>();
        if (CollUtil.isEmpty(jarPathList)) {
            return classFullNameList;
        }

        for (String jarPath : jarPathList) {
            try (JarFile jarFile = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory() || !entry.getName().endsWith(".class") || entry.getName().contains("$")) {
                        continue;
                    }
                    String className = entry.getName()
                            .substring(0, entry.getName().indexOf(".class"))
                            .replace("/", ".");
                    classFullNameList.add(className);
                }
            }
        }
        return classFullNameList;
    }

}
