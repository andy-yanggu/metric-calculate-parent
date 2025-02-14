package com.yanggu.metric_calculate.test.junit5.param;

import lombok.NoArgsConstructor;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * json参数提供者。反序列化成对象
 */
@NoArgsConstructor
public class JsonArgumentsProvider extends AnnotationBasedArgumentsProvider<JsonSource> implements ArgumentsProvider {

    @Override
    protected Stream<? extends Arguments> provideArguments(ExtensionContext context, JsonSource annotation) {
        String jsonArray = FileUtil.readUtf8String(annotation.value());
        return JSONUtil.toList(jsonArray, context.getRequiredTestMethod().getParameterTypes()[0])
                .stream()
                .map(Arguments::of);
    }

}