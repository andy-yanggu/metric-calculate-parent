package com.yanggu.metric_calculate.core2.util;


import com.yanggu.metric_calculate.core2.enums.AccuracyEnum;
import com.yanggu.metric_calculate.core2.pojo.metric.RoundAccuracy;
import org.junit.Test;

import static com.yanggu.metric_calculate.core2.util.RoundAccuracyUtil.handlerRoundAccuracy;
import static org.junit.Assert.*;

public class RoundAccuracyUtilTest {

    /**
     * 场景1、传入的数据为null, 应该返回null
     * 验证私有方法
     * 处理精度
     */
    @Test
    public void testObjectNull() throws Exception {
        Object result = handlerRoundAccuracy(null, null);
        assertNull(result);
    }

    /**
     * 场景2、传入的数据不是数值型
     * 应该原样返回
     */
    @Test
    public void testNotNumber() {
        Object result = new Object();
        Object invoke = handlerRoundAccuracy(result, null);
        assertEquals(result, invoke);
    }

    /**
     * 场景3、是数值型，但不处理精度
     * 没有给RoundAccuracy设置useAccuracy不应该报错
     * 应该原样返回
     */
    @Test
    public void testCase3() throws Exception {
        Long result = 10L;
        Object invoke = handlerRoundAccuracy(result, new RoundAccuracy());
        assertEquals(result, invoke);
    }

    /**
     * 场景4、是数值型，处理精度，设置useAccuracy为true，但是没有设置length
     * 应该原样返回
     *
     * @throws Exception
     */
    @Test
    public void testCase4() throws Exception {
        Long result = 10L;
        Object invoke = handlerRoundAccuracy(result, new RoundAccuracy().setUseAccuracy(true));
        assertEquals(result, invoke);
    }

    /**
     * 场景5、是数值型，处理精度，设置useAccuracy为true，设置length为-1
     * 应该原样返回
     *
     * @throws Exception
     */
    @Test
    public void testCase5() throws Exception {
        Long result = 10L;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setUseAccuracy(true).setLength(-1);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);
        assertEquals(result, invoke);
    }

    /**
     * 场景6、是数值型，处理精度，设置useAccuracy为true，设置length为2
     * 但是没有设置type字段, 应该报错
     *
     * @return
     * @throws Exception
     */
    @Test
    public void testCase6() throws Exception {
        Long result = 10L;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setUseAccuracy(true).setLength(2);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            handlerRoundAccuracy(result, roundAccuracy);
        });
        assertEquals("传入的type有误null", runtimeException.getMessage());
    }

    /**
     * 场景7、是数值型，处理精度，设置useAccuracy为true，设置length为2，设置type字段为1
     *
     * @return
     * @throws Exception
     */
    @Test
    public void testCase7() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setUseAccuracy(true).setLength(2).setType(AccuracyEnum.ROUNDING);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123四舍五入和保留两位小数, 应该得到10.12
        assertEquals("10.12", invoke);

    }

    /**
     * 场景8、是数值型，处理精度，设置useAccuracy为true，设置length为2，设置type字段为2
     *
     * @return
     * @throws Exception
     */
    @Test
    public void testCase8() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setUseAccuracy(true).setLength(2).setType(AccuracyEnum.KEEP_UP);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123向上保留和保留两位小数, 应该得到10.13
        assertEquals("10.13", invoke);

    }

    /**
     * 场景9、是数值型，处理精度，设置useAccuracy为true，设置length为0，设置type字段为1
     *
     * @return
     * @throws Exception
     */
    @Test
    public void testCase9() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setUseAccuracy(true).setLength(0).setType(AccuracyEnum.ROUNDING);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123四舍五入和整数, 应该得到10
        assertEquals("10", invoke);

    }

    /**
     * 场景10、是数值型，处理精度，设置useAccuracy为true，设置length为0，设置type字段为2
     *
     * @return
     * @throws Exception
     */
    @Test
    public void testCase10() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setUseAccuracy(true).setLength(0).setType(AccuracyEnum.KEEP_UP);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123四舍五入和整数, 应该得到10
        assertEquals("11", invoke);

    }

}