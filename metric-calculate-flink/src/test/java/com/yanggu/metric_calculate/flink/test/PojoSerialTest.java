package com.yanggu.metric_calculate.flink.test;


import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import org.apache.flink.types.PojoTestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PojoSerialTest {

    @Test
    @Disabled("仅仅测试使用, 实际不要执行")
    void test1() {
        PojoTestUtils.assertSerializedAsPojo(Model.class);
    }

}
