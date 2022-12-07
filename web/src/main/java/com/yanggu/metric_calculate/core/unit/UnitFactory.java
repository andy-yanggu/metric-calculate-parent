package com.yanggu.metric_calculate.core.unit;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.client.magiccube.enums.BasicType.*;

@Data
@NoArgsConstructor
public class UnitFactory {

    private Map<String, Class<? extends MergedUnit>> methodReflection = new HashMap<>();

    private List<String> udafJarPathList;

    public UnitFactory(List<String> udafJarPathList) {
        this.udafJarPathList = udafJarPathList;
    }

    public void init() throws Exception {
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage("com.yanggu.metric_calculate.core.unit", classFilter);
        classSet.forEach(tempClazz -> {
            MergeType annotation = tempClazz.getAnnotation(MergeType.class);
            methodReflection.put(annotation.value(), (Class<? extends MergedUnit>) tempClazz);
        });

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

        //这里父类指定为系统类加载器, 子类加载可以访问父类加载器中加载的类
        try (URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader())) {
            for (JarEntry entry : jarEntries) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                    String entryName = entry.getName().substring(0, entry.getName().indexOf(".class"))
                            .replace("/", ".");
                    Class<?> loadClass = urlClassLoader.loadClass(entryName);
                    if (loadClass.isAnnotationPresent(MergeType.class)) {
                        MergeType annotation = loadClass.getAnnotation(MergeType.class);
                        methodReflection.put(annotation.value(), (Class<? extends MergedUnit>) loadClass);
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
    public MergedUnit initInstanceByValue(String mergeable, Object initValue) throws Exception {
        Class clazz = getMergeableClass(mergeable);
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
        String canonicalPath = new File("").getCanonicalPath();
        String pathname = canonicalPath + "/udf-test/target/udf-test-1.0.0-SNAPSHOT.jar";
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList(pathname));
        unitFactory.init();
        MergedUnit count2 = unitFactory.initInstanceByValue("COUNT2", 1L);
        System.out.println(count2);

        MergedUnit sum2 = unitFactory.initInstanceByValue("SUM2", 2L);
        System.out.println(sum2);
    }

}
