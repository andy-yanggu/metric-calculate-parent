package com.yanggu.metric_calculate.core.fieldprocess.multi_field_order;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 多字段排序单元测试类
 */
public class MultiFieldOrderCompareKeyTest {

    @Test
    public void compareTo() {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey1 = new MultiFieldOrderCompareKey();
        List<FieldOrder> fieldOrderList1 = new ArrayList<>();
        fieldOrderList1.add(new FieldOrder().setResult(1).setAsc(true));
        fieldOrderList1.add(new FieldOrder().setResult(20).setAsc(false));
        multiFieldOrderCompareKey1.setFieldOrderList(fieldOrderList1);

        MultiFieldOrderCompareKey multiFieldOrderCompareKey2 = new MultiFieldOrderCompareKey();
        List<FieldOrder> fieldOrderList2 = new ArrayList<>();
        fieldOrderList2.add(new FieldOrder().setResult(1).setAsc(true));
        fieldOrderList2.add(new FieldOrder().setResult(30).setAsc(false));
        multiFieldOrderCompareKey2.setFieldOrderList(fieldOrderList2);

        int i = multiFieldOrderCompareKey1.compareTo(multiFieldOrderCompareKey2);
        assertEquals(1, i);

    }

    @Test
    public void fastClone() {
    }
}