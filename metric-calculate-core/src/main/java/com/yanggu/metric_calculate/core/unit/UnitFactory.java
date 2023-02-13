package com.yanggu.metric_calculate.core.unit;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.annotation.*;
import com.yanggu.metric_calculate.core.enums.BasicType;
import com.yanggu.metric_calculate.core.kryo.CoreKryoFactory;
import com.yanggu.metric_calculate.core.kryo.KryoUtils;
import com.yanggu.metric_calculate.core.number.*;
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
import com.yanggu.metric_calculate.core.unit.map.MapUnit;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.yanggu.metric_calculate.core.enums.BasicType.*;


@Data
@Slf4j
@NoArgsConstructor
public class UnitFactory implements Serializable {

    /**
     * 内置MergeUnit的包路径
     */
    public static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core.unit";

    private Map<String, Class<? extends MergedUnit<?>>> unitMap = new HashMap<>();

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
            log.debug("生成的unit: {}", JSONUtil.toJsonStr(unitMap));
        }
    }

    public Class<? extends MergedUnit<?>> getMergeableClass(String actionType) {
        Class<? extends MergedUnit<?>> clazz = unitMap.get(actionType.toUpperCase());
        if (clazz == null) {
            throw new RuntimeException("找不到聚合类型: " + actionType + "对应的clazz");
        }
        return clazz;
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
        Class clazz = unitMap.get(aggregateType);
        if (clazz == null) {
            throw new NullPointerException("MergedUnit class not found.");
        }
        if (clazz.isAnnotationPresent(Numerical.class)) {
            //数值型
            return createNumericUnit(clazz, initValue, params);
        } else if (clazz.isAnnotationPresent(Collective.class)) {
            //集合型
            return createCollectiveUnit(clazz, initValue, params);
        } else if (clazz.isAnnotationPresent(Objective.class)) {
            //对象型
            return createObjectiveUnit(clazz, initValue, params);
        } else if (clazz.isAnnotationPresent(MapType.class)) {
            //映射型
            return createMapUnit(clazz, initValue, params);
        } else {
            throw new RuntimeException(clazz.getName() + " not support.");
        }
    }

    private MergedUnit createMapUnit(Class<MapUnit> clazz, Object initValue, Map<String, Object> params) throws Exception {
        MapUnit mapUnit;
        if (useParam(clazz) && CollUtil.isNotEmpty(params)) {
            mapUnit = clazz.getConstructor(Map.class).newInstance(params);
        } else {
            mapUnit = clazz.getConstructor().newInstance();
        }
        Tuple tuple = (Tuple) initValue;
        mapUnit.put(tuple.get(0), tuple.get(1));
        return mapUnit;
    }

    /**
     * Create unit.
     */
    private MergedUnit createObjectiveUnit(Class<ObjectiveUnit> clazz,
                                           Object initValue,
                                           Map<String, Object> params) throws Exception {
        ObjectiveUnit objectiveUnit;
        if (useParam(clazz) && CollUtil.isNotEmpty(params)) {
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
        if (useParam(clazz) && CollUtil.isNotEmpty(params)) {
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
        if (useParam(clazz) && CollUtil.isNotEmpty(params)) {
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
                initArgs[0] = CubeLong.of(initValue);
                break;
            case DECIMAL:
                initArgs[0] = CubeDecimal.of(initValue);
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
        } else if (value instanceof Double) {
            return DECIMAL;
        } else if (value instanceof CubeDecimal) {
            return DECIMAL;
        } else if (value instanceof CubeDouble) {
            return DECIMAL;
        } else if (value instanceof CubeFloat) {
            return DECIMAL;
        } else if (value instanceof CubeLong) {
            return LONG;
        } else if (value instanceof CubeInteger) {
            return LONG;
        } else {
            throw new IllegalArgumentException(String.format("Not support type: %s", value.getClass().getName()));
        }
    }

    private void addClassToMap(Class<?> tempClazz) {
        MergeType annotation = tempClazz.getAnnotation(MergeType.class);
        Class<? extends MergedUnit<?>> put = unitMap.put(annotation.value(), (Class<? extends MergedUnit<?>>) tempClazz);
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
        count2.merge(count2.fastClone());
        System.out.println("count2 = " + count2);

        //测试Kryo序列化和反序列化自定义的udaf
        KryoPool kryoPool = KryoUtils.createRegisterKryoPool(new CoreKryoFactory());

        Kryo kryo = kryoPool.borrow();
        unitFactory.getUnitMap().values().forEach(kryo::register);

        //count2序列化生成的字节数组
        String string = "[1,99,111,109,46,121,97,110,103,103,117,46,109,101,116,114,105,99,95,99,97,108,99,117,108,97,116,101,46,99,111,114,101,46,116,101,115,116,95,117,110,105,116,46,67,111,117,110,116,85,110,105,116,-78,2,99,111,117,110,-12,118,97,108,117,-27,58,1,99,111,109,46,121,97,110,103,103,117,46,109,101,116,114,105,99,95,99,97,108,99,117,108,97,116,101,46,99,111,114,101,46,110,117,109,98,101,114,46,67,117,98,101,76,111,110,-25,1,118,97,108,117,-27,2,1,4,1,0,0,52,1,99,111,109,46,121,97,110,103,103,117,46,109,101,116,114,105,99,95,99,97,108,99,117,108,97,116,101,46,99,111,114,101,46,110,117,109,98,101,114,46,67,117,98,101,76,111,110,-25,2,1,4,1,0,0]";
        List<Byte> byteList = JSONUtil.toList(string, Byte.class);
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }

        Input input = new Input(bytes);
        Object result = kryo.readClassAndObject(input);
        System.out.println(result);
        MergedUnit mergedUnit = ((MergedUnit) result).merge(count2);
        System.out.println(mergedUnit);
    }

}
