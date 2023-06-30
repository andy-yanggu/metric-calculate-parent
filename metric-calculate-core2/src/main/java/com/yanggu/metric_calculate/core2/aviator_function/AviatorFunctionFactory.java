package com.yanggu.metric_calculate.core2.aviator_function;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@NoArgsConstructor
public class AviatorFunctionFactory {

    /**
     * 内置AbstractUdfAviatorFunction的包路径
     */
    public static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core2.aviator_function";

    private static final String ERROR_MESSAGE = "自定义聚合函数唯一标识重复, 重复的全类名: ";

    /**
     * 内置的AbstractUdfAviatorFunction
     */
    private static final Map<String, Class<? extends AbstractUdfAviatorFunction>> BUILT_IN_FUNCTION_MAP = new HashMap<>();

    private Map<String, Class<? extends AbstractUdfAviatorFunction>> functionMap = new HashMap<>();

    /**
     * udaf的jar包路径
     */
    private List<String> udafJarPathList;

    static {
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(AviatorFunctionName.class)
                && AbstractUdfAviatorFunction.class.isAssignableFrom(clazz);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, classFilter);
        for (Class<?> tempClazz : classSet) {
            //添加到内置的map中
            addClassToMap(tempClazz, BUILT_IN_FUNCTION_MAP);
        }
    }

    public AviatorFunctionFactory(List<String> udafJarPathList) {
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
            Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(AviatorFunctionName.class);
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
     * @param abstractUdfAviatorFunction
     * @param params
     */
    public static void setUdafParam(AbstractUdfAviatorFunction abstractUdfAviatorFunction,
                                    Map<String, Object> params) {
        Field[] declaredFields = abstractUdfAviatorFunction.getClass().getDeclaredFields();
        //通过反射给聚合函数的参数赋值
        if (CollUtil.isNotEmpty(params) && ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                Object fieldData = params.get(field.getName());
                if (fieldData != null) {
                    //通过反射给字段赋值
                    ReflectUtil.setFieldValue(abstractUdfAviatorFunction, field, fieldData);
                }
            }
        }
    }

    /**
     * 通过反射使用空参构造创建聚合函数
     *
     * @param aviatorFunctionName
     * @return
     */
    @SneakyThrows
    public AbstractUdfAviatorFunction getAviatorFunction(String aviatorFunctionName) {
        Class<? extends AbstractUdfAviatorFunction> clazz = getAviatorFunctionClass(aviatorFunctionName);
        return clazz.getDeclaredConstructor().newInstance();
    }

    private Class<? extends AbstractUdfAviatorFunction> getAviatorFunctionClass(String aggregate) {
        if (StrUtil.isBlank(aggregate)) {
            throw new RuntimeException("传入的聚合类型为空");
        }
        Class<? extends AbstractUdfAviatorFunction> clazz = functionMap.get(aggregate);
        if (clazz == null) {
            throw new RuntimeException("传入的" + aggregate + "有误");
        }
        return clazz;
    }

    private static void addClassToMap(Class<?> tempClazz, Map<String, Class<? extends AbstractUdfAviatorFunction>> functionMap) {
        AviatorFunctionName annotation = tempClazz.getAnnotation(AviatorFunctionName.class);
        if (annotation == null) {
            return;
        }
        String value = annotation.value();
        Class<? extends AbstractUdfAviatorFunction> put = functionMap.put(value, (Class<? extends AbstractUdfAviatorFunction>) tempClazz);
        if (put != null) {
            throw new RuntimeException(ERROR_MESSAGE + put.getName());
        }
    }

}
