package com.yanggu.metric_calculate.core2.test;


import cn.hutool.core.collection.CollUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AviatorTest {

    @Test
    public void test1() {
        String express =
                "let map = seq.map('2023-05-06', seq.list('test1'), '2023-05-07', seq.list('test2', 'test2', 'test3'), '2023-05-08', seq.list('test1'));\n" +
                        "seq.remove(map, '2023-05-06');\n" +
                        "let values = seq.vals(map);\n" +
                        "let set = seq.set();\n" +
                        "for tempSet in values {\n" +
                        "\tseq.add_all(set, tempSet);\n" +
                        "}\n" +
                        "return set;";
        Expression compile = AviatorEvaluator.compile(express);
        Object execute = compile.execute();
        assertEquals(CollUtil.newHashSet("test1", "test2", "test3"),execute);
    }

}
