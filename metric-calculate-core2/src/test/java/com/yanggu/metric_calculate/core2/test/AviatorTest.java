package com.yanggu.metric_calculate.core2.test;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.seq.MapSequence;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AviatorTest {

    /**
     * 过去30天的交易账号数据，不包含当天
     */
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
        assertEquals(CollUtil.newHashSet("test1", "test2", "test3"), execute);
    }

    /**
     * 转出账户过去30个自然日内每个自然日的转出笔数≥10笔的天数
     */
    @Test
    public void test2() {
        //底层使用的是Map, key是日期, value是次数
        Map<String, Integer> dayCount = new HashMap<>();
        dayCount.put("2023-05-15", 20);
        dayCount.put("2023-05-12", 5);

        String express =
                "return count(filter(dayCount, lambda (entry) -> getValue(entry) >= 10 end));";
        AviatorEvaluator.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
        Expression compile = AviatorEvaluator.compile(express);
        Map<String, Object> env = new HashMap<>();
        env.put("dayCount", new MapSequence(dayCount));

        Object execute = compile.execute(env);
        assertEquals(1L, execute);
    }

    /**
     * 转出账户过去30个自然日内转出金额为小额整数（小额整数：金额<500且金额能够被10整除）的笔数
     */
    @Test
    public void test3() {
        //主要考察前置过滤条件
        String express = "amount < 500 && amount % 10 == 0";

        AviatorEvaluator.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
        Expression compile = AviatorEvaluator.compile(express);

        Map<String, Object> env = new HashMap<>();
        env.put("amount", 200);
        Object execute = compile.execute(env);
        assertTrue((Boolean) execute);

        env.put("amount", 201);
        execute = compile.execute(env);
        assertFalse((Boolean) execute);

        env.put("amount", 500);
        execute = compile.execute(env);
        assertFalse((Boolean) execute);
    }

    @Test
    @Ignore
    public void test4() {
        int count = 0;
        int total = 1000000000;
        for (int j = 0; j < total; j++) {
            List<Integer> list = ListUtil.toList(RandomUtil.randomInt(0, 100), RandomUtil.randomInt(0, 100), RandomUtil.randomInt(0, 100));
            for (int i = 0; i < 12; i++) {
                boolean contains = list.contains(RandomUtil.randomInt(0, 100));
                if (contains) {
                    count++;
                    break;
                }
            }
            if (j != 0 && j % 10000000 == 0) {
                System.out.print("当前时间: "+ DateUtil.formatDateTime(new Date()) +"执行了: " + j + "次, ");
                System.out.println("命中次数: " + count * 1.0 / j);
            }
        }
        System.out.println("最后命中次数: " + count * 1.0 / total);
    }

    @Test
    public void test5() {
        String express = "add(a, 1)";
        Expression expression = AviatorEvaluator.getInstance().compile(express);
        Map<String, Object> stringObjectMap = expression.newEnv();
        System.out.println(stringObjectMap);
    }

    public static class Add extends AbstractFunction {

        @Override
        public String getName() {
            return "add";
        }

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            return null;
        }
    }

}
