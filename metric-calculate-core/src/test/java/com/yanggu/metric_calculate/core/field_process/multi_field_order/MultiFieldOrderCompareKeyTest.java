package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 多字段排序单元测试类
 */
class MultiFieldOrderCompareKeyTest {

    @Test
    void compareTo() {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey1 = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey1.setDataList(ListUtil.of(1, 20));

        MultiFieldOrderCompareKey multiFieldOrderCompareKey2 = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey2.setDataList(ListUtil.of(1, 30));

        List<Boolean> booleanList = ListUtil.of(true, false);
        multiFieldOrderCompareKey1.setBooleanList(booleanList);
        int i = multiFieldOrderCompareKey1.compareTo(multiFieldOrderCompareKey2);
        assertEquals(1, i);
    }

}