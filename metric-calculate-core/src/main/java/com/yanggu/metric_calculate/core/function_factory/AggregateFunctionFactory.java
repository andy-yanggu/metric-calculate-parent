package com.yanggu.metric_calculate.core.function_factory;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.dromara.hutool.core.classloader.JarClassLoader;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 聚合函数工厂类
 */
@NoArgsConstructor
public class AggregateFunctionFactory {

    /**
     * 内置AggregateFunction的包路径
     */
    public static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core.aggregate_function";
    /**
     * 扫描有AggregateFunctionAnnotation注解并且是AggregateFunction子类
     */
    public static final Predicate<Class<?>> CLASS_FILTER = clazz -> clazz.isAnnotationPresent(AggregateFunctionAnnotation.class)
            && AggregateFunction.class.isAssignableFrom(clazz);
    public static final JarClassLoader ACC_CLASS_LOADER = new JarClassLoader(new URL[0], ClassLoader.getSystemClassLoader());
    private static final String ERROR_MESSAGE = "自定义聚合函数唯一标识重复, 重复的全类名: ";
    /**
     * 内置的AggregateFunction
     */
    private static final Map<String, Class<? extends AggregateFunction>> BUILT_IN_FUNCTION_MAP = new HashMap<>();

    static {
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, CLASS_FILTER);
        for (Class<?> tempClazz : classSet) {
            //添加到内置的map中
            addClassToMap(tempClazz, BUILT_IN_FUNCTION_MAP);
        }
    }

    private final Map<String, Class<? extends AggregateFunction>> functionMap = new HashMap<>();
    /**
     * udaf的jar包路径
     */
    private List<String> udafJarPathList;

    public AggregateFunctionFactory(List<String> udafJarPathList) {
        this.udafJarPathList = udafJarPathList;
    }

    /**
     * 通过反射给聚合函数设置参数
     *
     * @param aggregateFunction
     * @param params
     */
    public static <IN, ACC, OUT> void initAggregateFunction(AggregateFunction<IN, ACC, OUT> aggregateFunction,
                                                            Map<String, Object> params) {
        FunctionFactory.setParam(aggregateFunction, params);
        aggregateFunction.init();
    }

    @SneakyThrows
    private static void addClassToMap(Class<?> tempClazz, Map<String, Class<? extends AggregateFunction>> functionMap) {
        if (!CLASS_FILTER.test(tempClazz)) {
            return;
        }
        String value = tempClazz.getAnnotation(AggregateFunctionAnnotation.class).name();
        //使用JarClassLoader统一加载ACC，便于Kryo的序列化和反序列化
        Method createAccumulatorMethod = MethodUtil.getMethodByName(tempClazz, "createAccumulator");
        if (createAccumulatorMethod != null) {
            Class<?> accumulatorClass = createAccumulatorMethod.getReturnType();
            ACC_CLASS_LOADER.loadClass(accumulatorClass.getName());
        }
        Class<? extends AggregateFunction> put = functionMap.put(value, (Class<? extends AggregateFunction>) tempClazz);
        if (put != null) {
            throw new RuntimeException(ERROR_MESSAGE + put.getName());
        }
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

        udafJarPathList.forEach(tempPath -> ACC_CLASS_LOADER.addURL(new File(tempPath)));

        //支持添加自定义的聚合函数
        FunctionFactory.loadClassFromJar(udafJarPathList, CLASS_FILTER, loadClass -> addClassToMap(loadClass, functionMap));
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
        return clazz.getDeclaredConstructor().newInstance();
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

}
