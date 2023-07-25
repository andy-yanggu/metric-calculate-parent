package com.yanggu.metric_calculate.core2.aviator_function;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core2.util.FunctionFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class AviatorFunctionFactory {

    /**
     * 内置AbstractUdfAviatorFunction的包路径
     */
    private static final String SCAN_PACKAGE = "com.yanggu.metric_calculate.core2.aviator_function";

    private static final String ERROR_MESSAGE = "自定义Aviator函数一标识重复, 重复的全类名: ";

    /**
     * 内置的AbstractUdfAviatorFunction
     */
    private static final Map<String, Class<? extends AbstractUdfAviatorFunction>> BUILT_IN_FUNCTION_MAP = new HashMap<>();

    /**
     * 扫描有AviatorFunctionName注解且是AbstractUdfAviatorFunction子类的类
     */
    private static final Filter<Class<?>> CLASS_FILTER = clazz -> clazz.isAnnotationPresent(AviatorFunctionName.class)
            && AbstractUdfAviatorFunction.class.isAssignableFrom(clazz);

    private final Map<String, Class<? extends AbstractUdfAviatorFunction>> functionMap = new HashMap<>();

    /**
     * udf的jar包路径
     */
    @Setter
    private List<String> udfJarPathList;

    static {
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, CLASS_FILTER);
        if (CollUtil.isNotEmpty(classSet)) {
            for (Class<?> tempClazz : classSet) {
                //添加到内置的map中
                addClassToMap(tempClazz, BUILT_IN_FUNCTION_MAP);
            }
        }
    }

    public AviatorFunctionFactory(List<String> udfJarPathList) {
        this.udfJarPathList = udfJarPathList;
    }

    /**
     * 添加系统自带的聚合函数和用户自定义的聚合函数
     *
     * @throws Exception
     */
    public void init() throws Exception {
        //放入内置的BUILT_IN_UNIT_MAP
        functionMap.putAll(BUILT_IN_FUNCTION_MAP);

        //自定义的udf的jar路径
        if (CollUtil.isEmpty(udfJarPathList)) {
            return;
        }

        //加载jar包中的自定义函数, 并添加到functionMap中
        FunctionFactory.loadClassFromJar(udfJarPathList, CLASS_FILTER, loadClass -> addClassToMap(loadClass, functionMap));
    }

    /**
     * 通过反射使用空参构造创建聚合函数
     *
     * @param aviatorFunctionName
     * @return
     */
    @SneakyThrows
    public AbstractUdfAviatorFunction getAviatorFunction(String aviatorFunctionName) {
        if (StrUtil.isBlank(aviatorFunctionName)) {
            throw new RuntimeException("传入的Aviator函数名为空");
        }
        Class<? extends AbstractUdfAviatorFunction> clazz = functionMap.get(aviatorFunctionName);
        if (clazz == null) {
            throw new RuntimeException("传入的" + aviatorFunctionName + "有误");
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * 通过反射给聚合函数设置参数
     *
     * @param abstractUdfAviatorFunction
     * @param params
     */
    public static void init(AbstractUdfAviatorFunction abstractUdfAviatorFunction,
                            Map<String, Object> params) {
        FunctionFactory.setParam(abstractUdfAviatorFunction, params);
        abstractUdfAviatorFunction.init();
    }

    private static void addClassToMap(Class<?> tempClazz,
                                      Map<String, Class<? extends AbstractUdfAviatorFunction>> functionMap) {
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
