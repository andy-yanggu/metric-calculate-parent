package com.yanggu.metric_calculate.core.test;


import org.codehaus.janino.*;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JaninoTest {

    @Test
    void test4() throws Exception {
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
    void test5() throws Exception {
        String[] strings = ExpressionEvaluator.guessParameterNames(new Scanner(null, new StringReader("a + b")));
        System.out.println(Arrays.toString(strings));
        Set<String> parameterNames = new HashSet<>(
                Arrays.asList(ExpressionEvaluator.guessParameterNames(new Scanner(null, new StringReader(
                        ""
                                + "import o.p;\n"
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
    void test6() throws Exception {
        String express = "x * x - x";
        Parser parser = new Parser(new Scanner(null, new StringReader(express)));
        Java.Rvalue rvalue = parser.parseExpression();
        System.out.println(rvalue);
    }

}
