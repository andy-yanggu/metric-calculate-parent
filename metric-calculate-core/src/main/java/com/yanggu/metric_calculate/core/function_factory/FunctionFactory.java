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
        if (CollUtil.isEmpty(jarPathList)) {
            return;
        }
        //加载jar包
        URL[] urls = new URL[jarPathList.size()];
        List<JarEntry> jarEntries = new ArrayList<>();
        for (int i = 0; i < jarPathList.size(); i++) {
            String jarPath = jarPathList.get(i);
            File file = new File(jarPath);
            urls[i] = file.toURI().toURL();

            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                jarEntries.add(entries.nextElement());
            }
        }

        //这里父类指定为系统类加载器, 子类加载可以访问父类加载器中加载的类
        //但是父类不可以访问子类加载器中加载的类, 线程上下文类加载器除外
        try (URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader())) {
            //过滤出对应的类
            for (JarEntry entry : jarEntries) {
                if (entry.isDirectory() || !entry.getName().endsWith(".class") || entry.getName().contains("$")) {
                    continue;
                }
                String className = entry.getName()
                        .substring(0, entry.getName().indexOf(".class"))
                        .replace("/", ".");
                Class<?> loadClass = urlClassLoader.loadClass(className);
                //判断是否应该进行消费
                if (classFilter.test(loadClass)) {
                    //消费class数据
                    consumer.accept(loadClass);
                }
            }
        }
    }

}
