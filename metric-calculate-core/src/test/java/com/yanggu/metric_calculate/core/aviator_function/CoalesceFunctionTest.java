package com.yanggu.metric_calculate.core.aviator_function;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorNil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class CoalesceFunctionTest {

    private CoalesceFunction coalesceFunction;

    @Before
    public void init() {
        coalesceFunction = new CoalesceFunction();
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("coalesce", coalesceFunction.getName());
    }

    /**
     * 测试两个参数的情况
     */
    @Test
    public void testCall_Two_Args() {
        Map<String, Object> env = Collections.emptyMap();
        Assert.assertEquals(AviatorNil.NIL, coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL));

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, new AviatorBigInt(2L), null)).longValue());
    }

    /**
     * 测试三个参数的情况
     */
    @Test
    public void testCall_Three_Args() {
        Map<String, Object> env = Collections.emptyMap();
        Assert.assertEquals(AviatorNil.NIL, coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, AviatorNil.NIL));

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, new AviatorBigInt(2L), null, null)).longValue());

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, AviatorNil.NIL, new AviatorBigInt(2L), null)).longValue());

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, new AviatorBigInt(2L))).longValue());
    }

    /**
     * 测试四个参数的情况
     */
    @Test
    public void testCall_Four_Args() {
        Map<String, Object> env = Collections.emptyMap();
        Assert.assertEquals(AviatorNil.NIL, coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, AviatorNil.NIL, AviatorNil.NIL));

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, new AviatorBigInt(2L), null, null, null)).longValue());

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, AviatorNil.NIL, new AviatorBigInt(2L), null, null)).longValue());

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, new AviatorBigInt(2L), null)).longValue());

        Assert.assertEquals(2L, ((AviatorLong) coalesceFunction.variadicCall(env, AviatorNil.NIL, AviatorNil.NIL, AviatorNil.NIL, new AviatorBigInt(2L))).longValue());
    }

    /**
     * 对于Aviator表达式表示null, 需要用Nil
     */
    @Test
    public void testExpress() {
        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
        instance.addFunction(coalesceFunction);

        //两个参数的情况
        Assert.assertNull(instance.execute("coalesce(Nil, Nil)"));
        Assert.assertEquals(1L, instance.execute("coalesce(1, Nil)"));
        Assert.assertEquals(2L, instance.execute("coalesce(Nil, 2)"));

        //三个参数的情况
        Assert.assertNull(instance.execute("coalesce(Nil, Nil, Nil)"));
        Assert.assertEquals(3L, instance.execute("coalesce(3, Nil, Nil)"));
        Assert.assertEquals(4L, instance.execute("coalesce(Nil, 4, Nil)"));
        Assert.assertEquals(5L, instance.execute("coalesce(Nil, Nil, 5)"));

        //四个参数的情况
        Assert.assertNull(instance.execute("coalesce(Nil, Nil, Nil, Nil)"));
        Assert.assertEquals(6L, instance.execute("coalesce(6, Nil, Nil, Nil)"));
        Assert.assertEquals(7L, instance.execute("coalesce(Nil, 7, Nil, Nil)"));
        Assert.assertEquals(8L, instance.execute("coalesce(Nil, Nil, 8, Nil)"));
        Assert.assertEquals(9L, instance.execute("coalesce(Nil, Nil, Nil, 9)"));
    }

    /**
     * 测试不是数值型的情况
     * 应该会报错
     */
    @Test
    public void testNoNumber() {
        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
        instance.addFunction(coalesceFunction);

        RuntimeException runtimeException = Assert.assertThrows(RuntimeException.class, () -> instance.execute("coalesce('test')"));
        Assert.assertEquals("传入的数据不是数值类型的test", runtimeException.getMessage());

    }

}
