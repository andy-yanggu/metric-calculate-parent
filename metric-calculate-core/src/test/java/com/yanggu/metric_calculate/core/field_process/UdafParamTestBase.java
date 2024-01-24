package com.yanggu.metric_calculate.core.field_process;


import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.dromara.hutool.core.array.ArrayUtil;

import java.util.Arrays;
import java.util.List;

public class UdafParamTestBase {

    public static BaseUdafParam createBaseUdafParam(String aggregateType,
                                                    String metricExpress,
                                                    String... metricExpressArray) {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType(aggregateType);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(metricExpress);
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        if (ArrayUtil.isNotEmpty(metricExpressArray)) {
            List<AviatorExpressParam> list = Arrays.stream(metricExpressArray)
                    .map(tempExpress -> {
                        AviatorExpressParam tempAviatorExpressParam = new AviatorExpressParam();
                        tempAviatorExpressParam.setExpress(tempExpress);
                        return tempAviatorExpressParam;
                    })
                    .toList();
            baseUdafParam.setMetricExpressParamList(list);
        }
        return baseUdafParam;
    }

}
