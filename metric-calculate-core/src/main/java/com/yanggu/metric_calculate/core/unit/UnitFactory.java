package com.yanggu.metric_calculate.core.unit;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.annotation.*;
import com.yanggu.metric_calculate.core.enums.BasicType;
import com.yanggu.metric_calculate.core.number.*;
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
import com.yanggu.metric_calculate.core.unit.map.MapUnit;
import com.yanggu.metric_calculate.core.unit.mix_unit.MixedUnit;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.unit.pattern.EventState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.janino.ScriptEvaluator;

import java.io.File;
import java.io.Serializable;
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

    /**
     * 内置的MergeUnit
     */
    private static final Map<String, Class<? extends MergedUnit<?>>> BUILT_IN_UNIT_MAP = new HashMap<>();

    /**
     * 内置的静态编译代码
     */
    private static final Map<String, ScriptEvaluator> BUILT_IN_EVALUATOR_MAP = new HashMap<>();

    private Map<String, Class<? extends MergedUnit<?>>> unitMap = new HashMap<>();

    private transient Map<String, ScriptEvaluator> evaluatorMap = new HashMap<>();

    /**
     * udaf的jar包路径
     */
    private List<String> udafJarPathList;

    public UnitFactory(List<String> udafJarPathList) {
        this.udafJarPathList = udafJarPathList;
    }

    //静态代码块只执行一次
    static {
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class)
                && MergedUnit.class.isAssignableFrom(clazz);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, classFilter);
        for (Class<?> tempClazz : classSet) {
            MergeType annotation = tempClazz.getAnnotation(MergeType.class);
            String value = annotation.value();
            Class<? extends MergedUnit<?>> put = BUILT_IN_UNIT_MAP.put(value, (Class<? extends MergedUnit<?>>) tempClazz);
            if (put != null) {
                throw new RuntimeException("自定义聚合函数唯一标识重复, 重复的全类名: " + put.getName());
            }
            //进行静态编译, 避免反射提高性能
            ScriptEvaluator janinoExpress = createJaninoExpress(tempClazz);
            BUILT_IN_EVALUATOR_MAP.put(value, janinoExpress);
        }
    }


    /**
     * 添加系统自带的聚合函数和用户自定义的聚合函数
     *
     * @throws Exception
     */
    public void init() throws Exception {
        //放入内置的BUILT_IN_UNIT_MAP
        BUILT_IN_UNIT_MAP.forEach((aggregateType, clazz) -> {
            Class<? extends MergedUnit<?>> put = unitMap.put(aggregateType, clazz);
            if (put != null) {
                throw new RuntimeException("自定义聚合函数唯一标识重复, 重复的全类名: " + put.getName());
            }
        });
        //放入内置的BUILT_IN_EVALUATOR_MAP
        evaluatorMap.putAll(BUILT_IN_EVALUATOR_MAP);

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
                if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                    String entryName = entry.getName().substring(0, entry.getName().indexOf(".class")).replace("/", ".");
                    Class<?> loadClass = urlClassLoader.loadClass(entryName);
                    if (classFilter.accept(loadClass)) {
                        addClassToMap(loadClass);
                        //动态生成Java代码和编译生成Janino表达式
                        ScriptEvaluator janinoExpress = createJaninoExpress(loadClass);
                        MergeType annotation = loadClass.getAnnotation(MergeType.class);
                        evaluatorMap.put(annotation.value(), janinoExpress);
                    }
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("生成的unit: {}", JSONUtil.toJsonStr(unitMap));
        }
    }

    /**
     * 使用Freemarker动态生成Java代码
     * <p>使用Janino进行编译, 避免反射生成MergedUnit</p>
     *
     * @param tempClass
     * @return
     */
    @SneakyThrows
    private static ScriptEvaluator createJaninoExpress(Class<?> tempClass) {

        Class<?> returnClass;
        int unitType;
        boolean multiNumber = false;
        Class<?> paramType = Object.class;

        MergeType mergeType = tempClass.getAnnotation(MergeType.class);
        if (tempClass.isAnnotationPresent(Numerical.class)) {
            //数值型
            returnClass = NumberUnit.class;
            unitType = 0;
            multiNumber = tempClass.getAnnotation(Numerical.class).multiNumber();
            if (multiNumber) {
                paramType = Tuple.class;
            }
        } else if (tempClass.isAnnotationPresent(Collective.class)) {
            //集合型
            returnClass = CollectionUnit.class;
            unitType = 1;
        } else if (tempClass.isAnnotationPresent(Objective.class)) {
            //对象型
            returnClass = ObjectiveUnit.class;
            unitType = 2;
        } else if (tempClass.isAnnotationPresent(MapType.class)) {
            //映射型
            returnClass = MapUnit.class;
            unitType = 3;
            paramType = Tuple.class;
        } else if (tempClass.isAnnotationPresent(Mix.class)) {
            //混合型
            returnClass = MixedUnit.class;
            unitType = 4;
            paramType = Map.class;
        } else if (tempClass.isAnnotationPresent(Pattern.class)) {
            //CEP类型
            returnClass = EventState.class;
            unitType = 5;
        } else {
            throw new RuntimeException(tempClass.getName() + " not support.");
        }

        //模板文件路径
        TemplateConfig templateConfig = new TemplateConfig("merged_unit_template", TemplateConfig.ResourceMode.CLASSPATH);
        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("merged_unit.ftl");

        Map<String, Object> param = new HashMap<>();

        //放入全类名
        param.put("fullName", tempClass.getName());
        //放入是否使用自定义参数
        param.put("useParam", mergeType.useParam());
        //放入聚合类型
        param.put("unitType", unitType);
        //放入数值型是否使用多参数
        param.put("multiNumber", multiNumber);

        //生成模板代码
        String templateCode = template.render(param);
        log.info("{}类生成的模板代码: {}\n", tempClass.getName(), templateCode);

        //编译表达式
        ScriptEvaluator evaluator = new ScriptEvaluator();
        String[] parameterNames = {"param", "initValue"};
        Class<?>[] parameterTypes = {Map.class, paramType};
        evaluator.setParameters(parameterNames, parameterTypes);
        evaluator.setReturnType(returnClass);
        evaluator.setParentClassLoader(tempClass.getClassLoader());
        evaluator.cook(templateCode);

        return evaluator;
    }

    public Class<? extends MergedUnit<?>> getMergeableClass(String actionType) {
        Class<? extends MergedUnit<?>> clazz = unitMap.get(actionType.toUpperCase());
        if (clazz == null) {
            throw new RuntimeException("找不到聚合类型: " + actionType + "对应的clazz");
        }
        return clazz;
    }

    /**
     * 使用Janino编译生成.class文件, 然后生成MergeUnit
     * <p>避免反射调用</p>
     *
     * @param aggregateType 聚合类型
     * @param initValue     度量值
     * @param params        自定义参数
     * @return
     * @throws Exception
     */
    public MergedUnit initInstanceByValue(String aggregateType, Object initValue, Map<String, Object> params) throws Exception {
        ScriptEvaluator scriptEvaluator = evaluatorMap.get(aggregateType);
        return (MergedUnit) scriptEvaluator.evaluate(params, initValue);
    }

    /**
     * 请注意, 该方法请不要删除和修改
     *
     * @param initValue
     * @return
     */
    @SneakyThrows
    public static CubeNumber<?> createCubeNumber(Object initValue) {
        if (!NumberUtil.isNumber(initValue.toString())) {
            throw new Exception("传入的不是数值");
        }
        BasicType valueType = ofValue(initValue);
        switch (valueType) {
            case LONG:
                return CubeLong.of(initValue);
            case DECIMAL:
                return CubeDecimal.of(initValue);
            default:
                throw new IllegalStateException("Unexpected value type: " + valueType);
        }
    }

    public static BasicType ofValue(Object value) {
        if (value instanceof Integer) {
            return LONG;
        } else if (value instanceof Long) {
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
        log.info("count2 = {}", count2);
    }

}
