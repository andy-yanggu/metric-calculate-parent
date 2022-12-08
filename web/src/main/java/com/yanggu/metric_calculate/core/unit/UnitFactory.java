package com.yanggu.metric_calculate.core.unit;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
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
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
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

import static com.yanggu.metric_calculate.client.magiccube.enums.BasicType.*;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Data
@NoArgsConstructor
public class UnitFactory {

    private Map<String, Class<? extends MergedUnit>> methodReflection = new HashMap<>();

    private Map<String, Class<? extends MergedUnit>> udafMethodReflection = new HashMap<>();

    private List<String> udafJarPathList;

    public UnitFactory(List<String> udafJarPathList) {
        this.udafJarPathList = udafJarPathList;
    }

    /**
     * 添加系统自带的聚合函数和用户自定义的聚合函数
     *
     * @throws Exception
     */
    public void init() throws Exception {
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage("com.yanggu.metric_calculate.core.unit", classFilter);
        classSet.forEach(this::addClassToMap);

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
            for (JarEntry entry : jarEntries) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                    String entryName = entry.getName().substring(0, entry.getName().indexOf(".class"))
                            .replace("/", ".");
                    Class<?> loadClass = urlClassLoader.loadClass(entryName);
                    if (classFilter.accept(loadClass)) {
                        addClassToUdafMap(loadClass);
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
        if (clazz.isAnnotationPresent(Numerical.class)) {
            return createNumericUnit(clazz, initValue);
        } else if (clazz.isAnnotationPresent(Collective.class)) {
            return createCollectiveUnit(clazz, initValue);
        } else if (clazz.isAnnotationPresent(Objective.class)) {
            return createObjectiveUnit(clazz, initValue);
        }
        throw new RuntimeException(clazz.getName() + " not support.");
    }

    public MergedUnit initInstanceByValueForUdaf(String mergeable, Object initValue, Map<String, Object> params) throws Exception {
        Class clazz = udafMethodReflection.get(mergeable);
        if (clazz == null) {
            throw new NullPointerException("MergedUnit class not found.");
        }
        MergeType mergeType = (MergeType) clazz.getAnnotation(MergeType.class);
        boolean useParam = mergeType.useParam();
        if (clazz.isAnnotationPresent(Numerical.class)) {
            if (useParam) {
                return createNumericUnitForUdaf(clazz, initValue, params);
            } else {
                return createNumericUnit(clazz, initValue);
            }
        } else if (clazz.isAnnotationPresent(Collective.class)) {
            if (useParam) {
                return createCollectiveUnitForUdaf(clazz, initValue, params);
            } else {
                return createCollectiveUnit(clazz, initValue);
            }
        } else if (clazz.isAnnotationPresent(Objective.class)) {
            if (useParam) {
                return createObjectiveUnitForUdaf(clazz, initValue, params);
            } else {
                return createObjectiveUnit(clazz, initValue);
            }
        }
        throw new RuntimeException(clazz.getName() + " not support.");
    }

    /**
     * Create unit.
     */
    public MergedUnit createObjectiveUnit(Class<ObjectiveUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().value(initValue);
    }

    /**
     * Create unit.
     */
    public MergedUnit createObjectiveUnitForUdaf(Class<ObjectiveUnit> clazz, Object initValue, Map<String, Object> params) throws Exception {
        Constructor<ObjectiveUnit> constructor = clazz.getConstructor(Map.class);
        return constructor.newInstance(params).value(initValue);
    }

    /**
     * Create collective unit.
     */
    public MergedUnit createCollectiveUnit(Class<CollectionUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().add(initValue);
    }

    /**
     * Create collective unit.
     */
    public MergedUnit createCollectiveUnitForUdaf(Class<CollectionUnit> clazz, Object initValue, Map<String, Object> params) throws Exception {
        Constructor<CollectionUnit> constructor = clazz.getConstructor(Map.class);
        return constructor.newInstance(params).add(initValue);
    }

    /**
     * Create number unit.
     */
    public NumberUnit createNumericUnit(Class<NumberUnit> clazz, Object initValue) throws Exception {
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

    /**
     * Create number unit.
     */
    public NumberUnit createNumericUnitForUdaf(Class<NumberUnit> clazz, Object initValue, Map<String, Object> params) throws Exception {
        Constructor<NumberUnit> constructor = clazz.getConstructor(CubeNumber.class, Map.class);
        BasicType valueType = ofValue(initValue);
        switch (valueType) {
            case LONG:
                return constructor.newInstance(CubeLong.of((Number) initValue), params);
            case DECIMAL:
                return constructor.newInstance(CubeDecimal.of((Number) initValue), params);
            default:
                throw new IllegalStateException("Unexpected value type: " + valueType);
        }
    }

    public BasicType ofValue(Object value) {
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

    private void addClassToMap(Class<?> tempClazz) {
        MergeType annotation = tempClazz.getAnnotation(MergeType.class);
        Class<? extends MergedUnit> put = methodReflection.put(annotation.value(), (Class<? extends MergedUnit>) tempClazz);
        if (put != null) {
            throw new RuntimeException("自定义聚合函数唯一标识重复, 重复的全类名: " + put.getName());
        }
    }

    private void addClassToUdafMap(Class<?> tempClazz) {
        MergeType annotation = tempClazz.getAnnotation(MergeType.class);
        Class<? extends MergedUnit> put = udafMethodReflection.put(annotation.value(), (Class<? extends MergedUnit>) tempClazz);
        if (put != null) {
            throw new RuntimeException("自定义聚合函数唯一标识重复, 重复的全类名: " + put.getName());
        }
    }

    public static void main(String[] args) throws Exception {
        String canonicalPath = new File("").getCanonicalPath();
        String pathname = canonicalPath + "/udaf-test/target/udaf-test-1.0.0-SNAPSHOT.jar";
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList(pathname));
        unitFactory.init();
        MergedUnit count2 = unitFactory.initInstanceByValueForUdaf("COUNT2", 1L, null);
        System.out.println(count2);

        MergedUnit sum2 = unitFactory.initInstanceByValueForUdaf("SUM2", 2L, null);
        System.out.println(sum2);


        HashMap<String, Object> params = new HashMap<>();
        //一天内3小时内的交易比例0.6, 如果大于0.6就返回1, 否则返回0
        params.put("ratio", 0.6D);
        params.put("continueHour", 3);

        long currentTimeMillis = DateUtil.parseDateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:00:00")).getTime();
        NumberUnit myUnit = (NumberUnit) unitFactory.initInstanceByValueForUdaf("MYUNIT", currentTimeMillis, params);

        //只有一条交易, 应该返回1
        System.out.println(myUnit.value());

        //新的交易在3小时外, 应该返回0
        myUnit.merge(unitFactory.initInstanceByValueForUdaf("MYUNIT", currentTimeMillis + HOURS.toMillis(3L), params));
        System.out.println(myUnit.value());

        //只有2条交易在3小时内 2.0 / 3 = 0.66 > 0.6
        myUnit.merge(unitFactory.initInstanceByValueForUdaf("MYUNIT", currentTimeMillis + HOURS.toMillis(2L), params));
        System.out.println(myUnit.value());

        //有3条交易在3小时内, 3.0 / 4 = 0.75 > 0.6
        myUnit.merge(unitFactory.initInstanceByValueForUdaf("MYUNIT", currentTimeMillis + HOURS.toMillis(2L) + MINUTES.toMillis(59L), params));
        System.out.println(myUnit.value());

    }

}
