package com.yanggu.metric_calculate.core2.util;


import com.yanggu.metric_calculate.core2.aggregate_function.collection.ListObjectAggregateFunction;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UdafCustomParamDataUtilTest {

    @Test
    public void testGetUdafCustomParamList_ListObjectUnit() {
        List<UdafCustomParamData> udafCustomParamDataList = UdafCustomParamUtil.getUdafCustomParamList(ListObjectAggregateFunction.class);
        assertEquals(1, udafCustomParamDataList.size());
        assertEquals("UdafCustomParamData(name=limit, dataType=Integer, defaultValue=10, update=false, notNull=false, description=)", udafCustomParamDataList.get(0).toString());
    }

}