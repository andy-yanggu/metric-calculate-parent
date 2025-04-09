package com.yanggu.metric_calculate.test.junit5.param;

import lombok.NoArgsConstructor;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * json参数提供者
 */
@NoArgsConstructor
public class JsonArgumentsProvider extends AnnotationBasedArgumentsProvider<JsonSource> implements ArgumentsProvider {

    @Override
    protected Stream<? extends Arguments> provideArguments(ExtensionContext context, JsonSource annotation) {
        Method method = context.getRequiredTestMethod();
        Parameter[] parameters = method.getParameters();
        if (ArrayUtil.isEmpty(parameters)) {
            return Stream.empty();
        }
        String jsonArrayString = FileUtil.readUtf8String(annotation.value());
        if (StrUtil.isBlank(jsonArrayString)) {
            return Stream.empty();
        }
        JSONArray jsonArray = JSONUtil.parseArray(jsonArrayString);
        if (JSONUtil.isEmpty(jsonArray)) {
            return Stream.empty();
        }
        // 如果只有一个参数，并且需要聚合，则直接返回一个参数的集合
        if (parameters.length == 1 && annotation.aggregate()) {
            return JSONUtil.toList(jsonArray, method.getParameterTypes()[0])
                    .stream()
                    .map(Arguments::of);
        } else {
            // 多个参数，则返回多个参数的集合
            return jsonArray.stream()
                    .map(json -> {
                        Object[] args = Arrays.stream(parameters)
                                .map(parameter -> getParamData(json, parameter))
                                .toArray();
                        return Arguments.of(args);
                    });
        }
    }

    /**
     * 获取参数值
     */
    private Object getParamData(JSON json, Parameter parameter) {
        JsonParam jsonParam = parameter.getAnnotation(JsonParam.class);
        Object paramData;
        Type parameterizedType = parameter.getParameterizedType();
        //如果使用了注解，使用path取值
        String name = parameter.getName();
        if (jsonParam != null) {
            String path = jsonParam.value();
            //如果使用了path，使用name取值
            if (StrUtil.isBlank(path)) {
                path = name;
            }
            paramData = JSONUtil.getByPath(json, path, parameterizedType);
            //如果没有path取值，使用默认值
            if (paramData == null) {
                paramData = ConvertUtil.convert(parameterizedType, jsonParam.defaultValue());
            }
            //如果是必传参数，并且取值失败，抛出异常
            if (jsonParam.required() && ObjUtil.isEmpty(paramData)) {
                throw new IllegalArgumentException("参数" + name + "为必传参数");
            }
        } else {
            //如果没有使用注解，使用参数名取值
            paramData = JSONUtil.getByPath(json, name, parameterizedType);
        }
        return paramData;
    }

}