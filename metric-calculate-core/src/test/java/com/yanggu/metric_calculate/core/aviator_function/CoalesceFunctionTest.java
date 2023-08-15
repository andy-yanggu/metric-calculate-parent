package com.yanggu.metric_calculate.core.aviator_function;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorNil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CoalesceFunctionTest {

    private CoalesceFunction coalesceFunction;

    @BeforeEach
    void init() {
        coalesceFunction = new CoalesceFunction();
    }

    @Test
    void testGetName() {
        assertEquals("coalesce", coalesceFunction.getName());
    }

    /**
     * 测试两个参数的情况
     */
    @Test
    void testCall_Two_Args() {
        Map<String, Object> env = Collections.emptyMap();
        assertEquals(AviatorNil.NIL, coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL));

        assertEquals(2L, coalesceFunction.variadicCall(env, new AviatorBigInt(2L), null).getValue(null));
    }

    /**
     * 测试三个参数的情况
     */
    @Test
    void testCall_Three_Args() {
        Map<String, Object> env = Collections.emptyMap();
        assertEquals(AviatorNil.NIL, coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, AviatorNil.NIL));

        assertEquals(2L, coalesceFunction.variadicCall(env, new AviatorBigInt(2L), null, null).getValue(null));

        assertEquals(2L, coalesceFunction.variadicCall(env, AviatorNil.NIL, new AviatorBigInt(2L), null).getValue(null));

        assertEquals(2L, coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, new AviatorBigInt(2L)).getValue(null));
    }

    /**
     * 对于Aviator表达式表示null, 需要用Nil
     */
    @Test
    void testExpress() {
        AviatorEvaluator.addFunction(coalesceFunction);

        //两个参数的情况
        assertNull(AviatorEvaluator.execute("coalesce(Nil, Nil)"));
        assertEquals(1L, AviatorEvaluator.execute("coalesce(1, Nil)"));
        assertEquals(2L, AviatorEvaluator.execute("coalesce(Nil, 2)"));

        //三个参数的情况
        assertNull(AviatorEvaluator.execute("coalesce(Nil, Nil, Nil)"));
        assertEquals(3L, AviatorEvaluator.execute("coalesce(3, Nil, Nil)"));
        assertEquals(4L, AviatorEvaluator.execute("coalesce(Nil, 4, Nil)"));
        assertEquals(5L, AviatorEvaluator.execute("coalesce(Nil, Nil, 5)"));

        //四个参数的情况
        assertNull(AviatorEvaluator.execute("coalesce(Nil, Nil, Nil, Nil)"));
        assertEquals(6L, AviatorEvaluator.execute("coalesce(6, Nil, Nil, Nil)"));
        assertEquals(7L, AviatorEvaluator.execute("coalesce(Nil, 7, Nil, Nil)"));
        assertEquals(8L, AviatorEvaluator.execute("coalesce(Nil, Nil, 8, Nil)"));
        assertEquals(9L, AviatorEvaluator.execute("coalesce(Nil, Nil, Nil, 9)"));
    }

}
