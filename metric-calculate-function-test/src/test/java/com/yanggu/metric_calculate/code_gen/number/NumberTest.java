package com.yanggu.metric_calculate.code_gen.number;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.codehaus.commons.compiler.util.ResourceFinderClassLoader;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.MapResourceFinder;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.StringResource;
import org.codehaus.janino.Compiler;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.TemplateUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("测试数值类型聚合函数codegen方式")
class NumberTest {

    @Test
    @DisplayName("测试使用模板生成代码并进行编译")
    void test1() throws Exception {
        TemplateConfig templateConfig = new TemplateConfig("aggregate_function_template", TemplateConfig.ResourceMode.CLASSPATH);
        TemplateEngine engine = TemplateUtil.getEngine().init(templateConfig);
        Template template = engine.getTemplate("sum.ftl");

        Map<String, Object> param = new HashMap<>();

        //放入全类名
        param.put("class_name", "Double");

        //生成模板代码
        String templateCode = template.render(param);
        //System.out.println(templateCode);

        //使用janino进行内存编译源码为class
        Compiler compiler = new Compiler();

        Map<String, byte[]> classes = new HashMap<>();
        compiler.setClassFileCreator((new MapResourceCreator(classes)));

        compiler.compile(new Resource[]{new StringResource("com.yanggu.metric_calculate.core.aggregate_function.numeric/Sum_Double_AggregateFunction.java", templateCode)});

        ClassLoader classLoader = new ResourceFinderClassLoader(
                new MapResourceFinder(classes),
                ClassLoader.getSystemClassLoader()
        );
        //使用自定义类加载器加载编译后的类
        Class<?> clazz = classLoader.loadClass("com.yanggu.metric_calculate.core.aggregate_function.numeric.Sum_Double_AggregateFunction");
        AggregateFunction<Double, Double, Double> aggregateFunction = (AggregateFunction<Double, Double, Double>) clazz.getDeclaredConstructor().newInstance();
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1.0D, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        assertEquals(1.0D, result, 0.0D);

        Double accumulator1 = aggregateFunction.createAccumulator();
        Double merge = aggregateFunction.merge(accumulator1, accumulator);
        assertEquals(1.0D, merge, 0.0D);
    }

}
