package com.yanggu.metric_calculate.core.util;


import com.yanggu.metric_calculate.core.aggregate_function.collection.ListObjectAggregateFunction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UdafCustomParamDataUtilTest {

    @Test
    void testGetUdafCustomParamList_ListObjectUnit() {
        List<UdafCustomParamData> udafCustomParamDataList = UdafCustomParamUtil.getUdafCustomParamList(ListObjectAggregateFunction.class);
        assertEquals(1, udafCustomParamDataList.size());
        assertEquals("UdafCustomParamData(name=limit, displayName=null, description=, dataType=Integer, defaultValue=10, update=false, notNull=false)", udafCustomParamDataList.get(0).toString());
    }

}