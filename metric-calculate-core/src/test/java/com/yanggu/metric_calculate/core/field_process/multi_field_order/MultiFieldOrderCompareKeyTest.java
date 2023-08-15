package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.ComparatorChain;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 多字段排序单元测试类
 */
class MultiFieldOrderCompareKeyTest {

    @Test
    void compareTo() {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey1 = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey1.setDataList(CollUtil.toList(1, 20));

        MultiFieldOrderCompareKey multiFieldOrderCompareKey2 = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey2.setDataList(CollUtil.toList(1, 30));

        List<Boolean> booleanList = CollUtil.toList(true, false);
        ComparatorChain<List<Object>> comparatorChain = MultiFieldOrderCompareKey.getComparatorChain(booleanList);
        multiFieldOrderCompareKey1.setComparatorChain(comparatorChain);
        int i = multiFieldOrderCompareKey1.compareTo(multiFieldOrderCompareKey2);
        //assertEquals(1, i);
    }

}