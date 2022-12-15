package com.yanggu.metric_calculate.core.unit;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
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
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@NoArgsConstructor
public class UnitFactory {

    /**
     * 内置MergeUnit的包路径
     */
    public static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core.unit";

    private Map<String, Class<? extends MergedUnit>> methodReflection = new HashMap<>();

    /**
     * udaf的jar包路径
     */
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
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, classFilter);
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
                    String entryName = entry.getName().substring(0, entry.getName().indexOf(".class")).replace("/", ".");
                    Class<?> loadClass = urlClassLoader.loadClass(entryName);
                    if (classFilter.accept(loadClass)) {
                        addClassToMap(loadClass);
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("生成的unit: {}", JSONUtil.toJsonStr(methodReflection));
        }
    }

    /**
     * 生成mergeUnit
     *
     * @param aggregateType 聚合类型
     * @param initValue     度量值
     * @param params        自定义参数
     * @return
     * @throws Exception
     */
    public MergedUnit initInstanceByValue(String aggregateType, Object initValue, Map<String, Object> params) throws Exception {
        Class clazz = methodReflection.get(aggregateType);
        if (clazz == null) {
            throw new NullPointerException("MergedUnit class not found.");
        }
        if (clazz.isAnnotationPresent(Numerical.class)) {
            return createNumericUnit(clazz, initValue, params);
        } else if (clazz.isAnnotationPresent(Collective.class)) {
            return createCollectiveUnit(clazz, initValue, params);
        } else if (clazz.isAnnotationPresent(Objective.class)) {
            return createObjectiveUnit(clazz, initValue, params);
        } else {
            throw new RuntimeException(clazz.getName() + " not support.");
        }
    }

    /**
     * Create unit.
     */
    private MergedUnit createObjectiveUnit(Class<ObjectiveUnit> clazz,
                                           Object initValue,
                                           Map<String, Object> params) throws Exception {
        ObjectiveUnit objectiveUnit;
        if (useParam(clazz)) {
            objectiveUnit = clazz.getConstructor(Map.class).newInstance(params);
        } else {
            objectiveUnit = clazz.getConstructor().newInstance();
        }
        return objectiveUnit.value(initValue);
    }

    /**
     * Create collective unit.
     */
    private MergedUnit createCollectiveUnit(Class<CollectionUnit> clazz, Object initValue, Map<String, Object> params) throws Exception {
        CollectionUnit collectionUnit;
        if (useParam(clazz)) {
            collectionUnit = clazz.getConstructor(Map.class).newInstance(params);
        } else {
            collectionUnit = clazz.getConstructor().newInstance();
        }
        return collectionUnit.add(initValue);
    }

    /**
     * Create number unit.
     */
    private NumberUnit createNumericUnit(Class<NumberUnit> clazz, Object initValue, Map<String, Object> params) throws Exception {
        Constructor<NumberUnit> constructor;
        Object[] initArgs;
        if (useParam(clazz)) {
            //构造函数, 如果使用自定义参数
            //对于数值型是两个参数, 第一个是CubeNumber, 第二个是Map
            constructor = clazz.getConstructor(CubeNumber.class, Map.class);
            initArgs = new Object[2];
            initArgs[1] = params;
        } else {
            constructor = clazz.getConstructor(CubeNumber.class);
            initArgs = new Object[1];
        }
        //判断数据类型
        BasicType valueType = ofValue(initValue);
        switch (valueType) {
            case LONG:
                initArgs[0] = CubeLong.of((Number) initValue);
                break;
            case DECIMAL:
                initArgs[0] = CubeDecimal.of((Number) initValue);
                break;
            default:
                throw new IllegalStateException("Unexpected value type: " + valueType);
        }
        return constructor.newInstance(initArgs);
    }

    private boolean useParam(Class<?> clazz) {
        MergeType mergeType = clazz.getAnnotation(MergeType.class);
        //是否使用自定义参数
        return mergeType.useParam();
    }

    private BasicType ofValue(Object value) {
        if (value instanceof Long) {
            return LONG;
        } else if (value instanceof String) {
            return STRING;
        } else if (value instanceof Boolean) {
            return BOOLEAN;
        } else if (value instanceof BigDecimal) {
            return DECIMAL;
        } else {
            throw new IllegalArgumentException(String.format("Not support type: %s", value.getClass().getName()));
        }
    }

    private void addClassToMap(Class<?> tempClazz) {
        MergeType annotation = tempClazz.getAnnotation(MergeType.class);
        Class<? extends MergedUnit> put = methodReflection.put(annotation.value(), (Class<? extends MergedUnit>) tempClazz);
        if (put != null) {
            throw new RuntimeException("自定义聚合函数唯一标识重复, 重复的全类名: " + put.getName());
        }
    }

    public static void main(String[] args) throws Exception {
        String canonicalPath = new File("").getCanonicalPath();
        String pathname = canonicalPath + "/udaf-test/target/udaf-test-1.0.0-SNAPSHOT.jar";
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList(pathname));
        unitFactory.init();
        MergedUnit count2 = unitFactory.initInstanceByValue("COUNT2", 1L, null);
        System.out.println(count2);

        MergedUnit sum2 = unitFactory.initInstanceByValue("SUM2", 2L, null);
        System.out.println(sum2);


        HashMap<String, Object> params = new HashMap<>();
        //一天内3小时内的交易比例0.6, 如果大于0.6就返回1, 否则返回0
        params.put("ratio", 0.6D);
        params.put("continueHour", 3);

        long currentTimeMillis = DateUtil.parseDateTime("2022-12-09 11:06:11").getTime();
        NumberUnit myUnit = (NumberUnit) unitFactory.initInstanceByValue("MYUNIT", currentTimeMillis, params);

        //只有一条交易, 应该返回1
        System.out.println(myUnit.value());

        //新的交易在3小时外, 应该返回0
        myUnit.merge(unitFactory.initInstanceByValue("MYUNIT", currentTimeMillis + HOURS.toMillis(3L), params));
        System.out.println(myUnit.value());

        //只有2条交易在3小时内 2.0 / 3 = 0.66 > 0.6
        myUnit.merge(unitFactory.initInstanceByValue("MYUNIT", currentTimeMillis + HOURS.toMillis(2L), params));
        System.out.println(myUnit.value());

        //有3条交易在3小时内, 3.0 / 4 = 0.75 > 0.6
        myUnit.merge(unitFactory.initInstanceByValue("MYUNIT", currentTimeMillis + HOURS.toMillis(2L) + MINUTES.toMillis(59L), params));
        System.out.println(myUnit.value());

    }

}
