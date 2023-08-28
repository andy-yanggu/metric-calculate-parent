package com.yanggu.metric_calculate.core.util;


import com.yanggu.metric_calculate.core.enums.AccuracyEnum;
import com.yanggu.metric_calculate.core.pojo.metric.RoundAccuracy;
import org.junit.jupiter.api.Test;

import static com.yanggu.metric_calculate.core.enums.AccuracyEnum.NOT_HANDLE;
import static com.yanggu.metric_calculate.core.enums.AccuracyEnum.ROUNDING;
import static com.yanggu.metric_calculate.core.util.RoundAccuracyUtil.handlerRoundAccuracy;
import static org.junit.jupiter.api.Assertions.*;

class RoundAccuracyUtilTest {

    /**
     * 场景1、传入的数据为null, 应该返回null
     * 验证私有方法
     * 处理精度
     */
    @Test
    void testObjectNull() throws Exception {
        Object result = handlerRoundAccuracy(null, null);
        assertNull(result);
    }

    /**
     * 场景2、传入的数据不是数值型
     * 应该原样返回
     */
    @Test
    void testNotNumber() {
        Object result = new Object();
        Object invoke = handlerRoundAccuracy(result, null);
        assertEquals(result, invoke);
    }

    /**
     * 场景3、是数值型，但不处理精度
     * 应该原样返回
     */
    @Test
    void testCase3() throws Exception {
        Long result = 10L;
        Object invoke = handlerRoundAccuracy(result, new RoundAccuracy().setType(NOT_HANDLE));
        assertEquals(result, invoke);
    }

    /**
     * 场景4、是数值型，处理精度，设置length为2，设置type字段为四舍五入
     *
     * @return
     * @throws Exception
     */
    @Test
    void testCase4() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setLength(2).setType(ROUNDING);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123四舍五入和保留两位小数, 应该得到10.12
        assertEquals("10.12", invoke);

    }

    /**
     * 场景5、是数值型，处理精度，设置length为2，设置type字段为向上保留
     *
     * @return
     * @throws Exception
     */
    @Test
    void testCase5() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setLength(2).setType(AccuracyEnum.KEEP_UP);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123向上保留和保留两位小数, 应该得到10.13
        assertEquals("10.13", invoke);

    }

    /**
     * 场景6、是数值型，处理精度，设置length为0，设置type字段为四舍五入
     *
     * @return
     * @throws Exception
     */
    @Test
    void testCase6() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setLength(0).setType(ROUNDING);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123四舍五入和整数, 应该得到10
        assertEquals("10", invoke);

    }

    /**
     * 场景7、是数值型，处理精度，设置length为0，设置type字段为向上保留
     *
     * @return
     * @throws Exception
     */
    @Test
    void testCase7() throws Exception {
        Double result = 10.123D;
        RoundAccuracy roundAccuracy = new RoundAccuracy().setLength(0).setType(AccuracyEnum.KEEP_UP);

        Object invoke = handlerRoundAccuracy(result, roundAccuracy);

        //10.123四舍五入和整数, 应该得到10
        assertEquals("11", invoke);
    }

}