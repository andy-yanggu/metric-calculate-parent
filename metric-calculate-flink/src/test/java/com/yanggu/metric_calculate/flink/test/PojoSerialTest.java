package com.yanggu.metric_calculate.flink.test;


import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import org.apache.flink.types.PojoTestUtils;
import org.junit.Test;

public class PojoSerialTest {

    @Test
    public void test1() {
        PojoTestUtils.assertSerializedAsPojo(DataDetailsWideTable.class);
    }

}
