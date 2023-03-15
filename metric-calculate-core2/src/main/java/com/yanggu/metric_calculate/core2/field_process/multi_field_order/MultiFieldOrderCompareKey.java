package com.yanggu.metric_calculate.core2.field_process.multi_field_order;

import com.google.common.collect.Ordering;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 多字段排序
 */
@Data
public class MultiFieldOrderCompareKey implements Comparable<MultiFieldOrderCompareKey> {

    public static final MultiFieldOrderCompareKey MAX = new MultiFieldOrderCompareKey();

    public static final MultiFieldOrderCompareKey MIN = new MultiFieldOrderCompareKey();

    private List<FieldOrder> fieldOrderList;

    @Override
    public int compareTo(@NonNull MultiFieldOrderCompareKey that) {
        if (that.equals(MAX)) {
            return -1;
        }
        if (that.equals(MIN)) {
            return 1;
        }
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
            int finalIndex = i;
            Ordering<List<FieldOrder>> ordering = comparableOrdering.onResultOf(
                    input -> (Comparable<?>) input.get(finalIndex).getResult());
            orderingList.add(ordering);
        }
        //合并多个比较器
        Ordering<List<FieldOrder>> multiFieldOrderOrdering  = Ordering.compound(orderingList);
        return multiFieldOrderOrdering.compare(fieldOrderList, that.fieldOrderList);
    }

}
