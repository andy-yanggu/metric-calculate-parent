package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import com.google.common.collect.Ordering;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 多字段排序
 */
@Data
@NoArgsConstructor
public class MultiFieldOrderCompareKey implements Comparable<MultiFieldOrderCompareKey> {

    private List<Object> dataList;

    private Ordering<List<Object>> multiFieldOrderOrdering;

    public MultiFieldOrderCompareKey(List<Object> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int compareTo(@NonNull MultiFieldOrderCompareKey that) {
        if (this.multiFieldOrderOrdering == null) {
            this.multiFieldOrderOrdering = that.multiFieldOrderOrdering;
        }
        return multiFieldOrderOrdering.compare(dataList, that.dataList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultiFieldOrderCompareKey that = (MultiFieldOrderCompareKey) o;

        return dataList.equals(that.dataList);
    }

    @Override
    public int hashCode() {
        return dataList.hashCode();
    }

    public static Ordering<List<Object>> getOrdering(List<Boolean> booleanList) {
        List<Ordering<List<Object>>> orderingList = new ArrayList<>();
        for (int i = 0; i < booleanList.size(); i++) {
            Boolean result = booleanList.get(i);
            Ordering<Comparable<?>> comparableOrdering;
            //降序排序
            if (Boolean.FALSE.equals(result)) {
                //降序时, null放在最后面
                comparableOrdering = Ordering.natural().reverse().nullsLast();
            } else {
                //升序排序, null放在最前面
                comparableOrdering = Ordering.natural().nullsFirst();
            }
            int finalIndex = i;
            Ordering<List<Object>> ordering = comparableOrdering.onResultOf(
                    input -> (Comparable<?>) input.get(finalIndex));
            orderingList.add(ordering);
        }
        //合并多个比较器
        return Ordering.compound(orderingList);
    }

}
