package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import com.google.common.collect.Ordering;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 多字段排序
 */
@Data
public class MultiFieldOrderCompareKey implements Comparable<MultiFieldOrderCompareKey>,
        Clone<MultiFieldOrderCompareKey>, Value<List<FieldOrder>> {

    private List<FieldOrder> fieldOrderList;

    @Override
    public int compareTo(@NonNull MultiFieldOrderCompareKey that) {
        List<Ordering<List<FieldOrder>>> orderingList = new ArrayList<>();
        for (int i = 0; i < fieldOrderList.size(); i++) {
            FieldOrder fieldOrder = fieldOrderList.get(i);
            Ordering<Comparable<?>> comparableOrdering;
            //降序排序
            if (Boolean.FALSE.equals(fieldOrder.getAsc())) {
                //降序时, null放在最后面
                comparableOrdering = Ordering.natural().reverse().nullsLast();
            } else {
                //升序排序, null放在最前面
                comparableOrdering = Ordering.natural().nullsFirst();
            }
            int finalI = i;
            Ordering<List<FieldOrder>> ordering = comparableOrdering.onResultOf(
                    input -> (Comparable<?>) input.get(finalI).getResult());
            orderingList.add(ordering);
        }
        //合并多个比较器
        Ordering<List<FieldOrder>> multiFieldOrderOrdering  = Ordering.compound(orderingList);
        return multiFieldOrderOrdering.compare(fieldOrderList, that.fieldOrderList);
    }

    @Override
    public MultiFieldOrderCompareKey fastClone() {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setFieldOrderList(fieldOrderList);
        return multiFieldOrderCompareKey;
    }

    @Override
    public List<FieldOrder> value() {
        return fieldOrderList;
    }

}
