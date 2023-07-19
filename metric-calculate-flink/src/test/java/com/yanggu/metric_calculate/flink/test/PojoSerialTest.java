package com.yanggu.metric_calculate.flink.test;


import com.yanggu.metric_calculate.core2.pojo.data_detail_table.Model;
import org.apache.flink.types.PojoTestUtils;
import org.junit.jupiter.api.Test;

public class PojoSerialTest {

    @Test
    public void test1() {
        PojoTestUtils.assertSerializedAsPojo(Model.class);
    }

}
