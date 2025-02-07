package com.yanggu.metric_calculate.core.util;


import com.yanggu.metric_calculate.core.enums.AccuracyEnum;
import com.yanggu.metric_calculate.core.pojo.metric.RoundAccuracy;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.math.NumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.yanggu.metric_calculate.core.enums.AccuracyEnum.NOT_HANDLE;

/**
 * 精度处理工具类
 */
@Slf4j
public class RoundAccuracyUtil {

    private RoundAccuracyUtil() {
    }

    public static Object handlerRoundAccuracy(Object result, RoundAccuracy roundAccuracy) {
        Object returnData = result;
        //如果结果为数值并且需要精度处理
        if (result instanceof Number && roundAccuracy != null
                && !roundAccuracy.getType().equals(NOT_HANDLE)
                && roundAccuracy.getLength() != null && roundAccuracy.getLength() >= 0) {
            AccuracyEnum type = roundAccuracy.getType();
            int length = roundAccuracy.getLength();

            //四舍五入
            if (AccuracyEnum.ROUNDING.equals(type)) {
                returnData = new BigDecimal(result.toString()).setScale(length, RoundingMode.HALF_UP).doubleValue();
            } else if (AccuracyEnum.KEEP_UP.equals(type)) {
                //向上保留
                returnData = new BigDecimal(result.toString()).setScale(length, RoundingMode.CEILING).doubleValue();
            } else {
                throw new RuntimeException("传入的type有误" + type);
            }
            //处理保留多少位
            if (length != 0) {
                returnData = NumberUtil.format("#." + "0".repeat(length), returnData);
            } else {
                //保留0位就是整数
                returnData = NumberUtil.format("#", returnData);
            }
            if (log.isDebugEnabled()) {
                log.debug("原始数据: {}, 精度配置: {}, 处理精度后的数据: {}", result, roundAccuracy, returnData);
            }
        }
        return returnData;
    }

}
