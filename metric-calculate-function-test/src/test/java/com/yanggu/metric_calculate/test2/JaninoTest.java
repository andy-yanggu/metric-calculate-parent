package com.yanggu.metric_calculate.test2;


import org.codehaus.commons.compiler.util.ResourceFinderClassLoader;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.MapResourceFinder;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.StringResource;
import org.codehaus.janino.Scanner;
import org.codehaus.janino.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Janino功能测试")
class JaninoTest {

    @Test
    @DisplayName("测试表达式引擎功能")
    void test1() throws Exception {
        ExpressionEvaluator ee = new ExpressionEvaluator();

        // The expression will have two "int" parameters: "a" and "b".
        ee.setParameters(new String[]{"a", "b"}, new Class[]{int.class, int.class});

        // And the expression (i.e. "result") type is also "int".
        ee.setExpressionType(int.class);

        // And now we "cook" (scan, parse, compile and load) the fabulous expression.
        ee.cook("a + b");

        int result = (Integer) ee.evaluate(new Object[]{12, 23});
        assertEquals(35, result);
    }

    @Test
    @DisplayName("测试编译器功能")
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

    @Test
    @DisplayName("测试依赖参数功能")
    void test3() throws Exception {
        String[] strings = ExpressionEvaluator.guessParameterNames(new Scanner(null, new StringReader("a + b")));
        assertArrayEquals(new String[]{"a", "b"}, strings);
        Set<String> parameterNames = new HashSet<>(
                Arrays.asList(ExpressionEvaluator.guessParameterNames(new Scanner(null, new StringReader(
                        "import o.p;\n"
                                + "a + b.c + d.e() + f() + g.h.I.j() + k.l.M"
                ))))
        );
        assertEquals(new HashSet<>(Arrays.asList("a", "b", "d")), parameterNames);

        parameterNames = new HashSet<>(
                Arrays.asList(ScriptEvaluator.guessParameterNames(new Scanner(null, new StringReader(
                        ""
                                + "import o.p;\n"
                                + "int a;\n"
                                + "return a + b.c + d.e() + f() + g.h.I.j() + k.l.M;"
                ))))
        );
        assertEquals(new HashSet<>(Arrays.asList("b", "d")), parameterNames);
    }

    @Test
    @DisplayName("简单测试词法和语法分析")
    void test4() throws Exception {
        String express = "x * x - x";
        Parser parser = new Parser(new Scanner(null, new StringReader(express)));
        Java.Rvalue rvalue = parser.parseExpression();
        System.out.println(rvalue);
    }

}
