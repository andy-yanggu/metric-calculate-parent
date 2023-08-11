package com.yanggu.metric_calculate.core.code_gen.number;


import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.codehaus.commons.compiler.util.ResourceFinderClassLoader;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.MapResourceFinder;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.StringResource;
import org.codehaus.janino.Compiler;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NumberTest {

    @Test
    void test1() throws Exception {
        TemplateConfig templateConfig = new TemplateConfig("merged_unit_template", TemplateConfig.ResourceMode.CLASSPATH);
        TemplateEngine engine = TemplateUtil.createEngine(templateConfig);
        Template template = engine.getTemplate("number.ftl");

        Map<String, Object> param = new HashMap<>();

        //放入全类名
        param.put("class_name", "Double");

        //生成模板代码
        String templateCode = template.render(param);
        System.out.println(templateCode);

        Compiler compiler = new Compiler();

        // Store generated .class files in a Map:
        Map<String, byte[]> classes = new HashMap<>();
        compiler.setClassFileCreator((new MapResourceCreator(classes)));

        compiler.compile(new Resource[] {new StringResource("com.yanggu.metric_calculate.core.aggregate_function.numeric/Sum_Double_AggregateFunction.java", templateCode)});

        ClassLoader cl = new ResourceFinderClassLoader(
                new MapResourceFinder(classes),  /// resourceFinder
                ClassLoader.getSystemClassLoader()  /// parent
        );
        Class<?> clazz = cl.loadClass("com.yanggu.metric_calculate.core.aggregate_function.numeric.Sum_Double_AggregateFunction");
        Object obj = clazz.getDeclaredConstructor().newInstance();
        AggregateFunction<Double, Double, Double> aggregateFunction = (AggregateFunction) obj;
        Double accumulator = aggregateFunction.createAccumulator();
        accumulator = aggregateFunction.add(1.0D, accumulator);
        Double result = aggregateFunction.getResult(accumulator);
        assertEquals(1.0D, result, 0.0D);

        Double accumulator1 = aggregateFunction.createAccumulator();
        Double merge = aggregateFunction.merge(accumulator1, accumulator);
        assertEquals(1.0D, merge, 0.0D);
    }

    @Test
    void test2() throws Exception {
        Compiler compiler = new Compiler();

        // Store generated .class files in a Map:
        Map<String, byte[]> classes = new HashMap<>();
        compiler.setClassFileCreator((new MapResourceCreator(classes)));

        // Now compile two units from strings:
        compiler.compile(new Resource[]{
                new StringResource(
                        "pkg1/A.java",
                        "package pkg1; public class A { public static int meth() { return pkg2.B.meth(); } }"
                ),
                new StringResource(
                        "pkg2/B.java",
                        "package pkg2; public class B { public static int meth() { return 77;            } }"
                ),
        });

        // Set up a class loader that uses the generated classes.
        ClassLoader cl = new ResourceFinderClassLoader(
                new MapResourceFinder(classes),  /// resourceFinder
                ClassLoader.getSystemClassLoader()  /// parent
        );
        assertEquals(77, cl.loadClass("pkg1.A").getDeclaredMethod(("meth")).invoke(null));
    }

}
