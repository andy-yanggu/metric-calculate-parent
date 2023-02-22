package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.unit.object.MaxFieldUnit;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UdafCustomParamDataUtilTest {

    @Test
    public void testGetUdafCustomParamList_ListObjectUnit() {
        List<UdafCustomParamData> udafCustomParamDataList = UdafCustomParamUtil.getUdafCustomParamList(ListObjectUnit.class);
        assertEquals(1, udafCustomParamDataList.size());
        assertEquals("UdafCustomParamData(name=limit, dataType=Integer, defaultValue=0, update=false, notNull=false, description=)", udafCustomParamDataList.get(0).toString());
    }

    @Test
    public void testGetUdafCustomParamList_MaxFieldUnit() {
        List<UdafCustomParamData> udafCustomParamDataList = UdafCustomParamUtil.getUdafCustomParamList(MaxFieldUnit.class);
        assertEquals(1, udafCustomParamDataList.size());
        assertEquals("UdafCustomParamData(name=onlyShowValue, dataType=Boolean, defaultValue=true, update=false, notNull=false, description=)", udafCustomParamDataList.get(0).toString());
    }


}