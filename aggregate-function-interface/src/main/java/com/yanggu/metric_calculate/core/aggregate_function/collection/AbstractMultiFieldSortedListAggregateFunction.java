package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 多字段有序列表抽象类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractMultiFieldSortedListAggregateFunction<IN, OUT> extends AbstractSortedListAggregateFunction<KeyValue<MultiFieldDistinctKey, IN>, OUT> {

    @AggregateFunctionFieldAnnotation(displayName = "升序和降序", notNull = true)
    private List<Boolean> booleanList;

    private ListObjectComparator comparator;

    @Override
    public void init() {
        ListObjectComparator tempComparator = this.new ListObjectComparator();
        tempComparator.setBooleanList(booleanList);
        this.comparator = tempComparator;
    }

    @Override
    public BoundedPriorityQueue<KeyValue<MultiFieldDistinctKey, IN>> createAccumulator() {
        return new BoundedPriorityQueue<>(getLimit(), comparator);
    }

    @Override
    public abstract OUT inToOut(KeyValue<MultiFieldDistinctKey, IN> multiFieldDistinctKeyINKeyValue);

    @Data
    class ListObjectComparator implements Comparator<KeyValue<MultiFieldDistinctKey, IN>> {

        private List<Boolean> booleanList;

        @Override
        public int compare(KeyValue<MultiFieldDistinctKey, IN> o1, KeyValue<MultiFieldDistinctKey, IN> o2) {
            List<Object> dataList1 = o1.getKey().getFieldList();
            List<Object> dataList2 = o2.getKey().getFieldList();

            for (int i = 0; i < booleanList.size(); i++) {
                Boolean asc = booleanList.get(i);
                Object data1 = dataList1.get(i);
                Object data2 = dataList2.get(i);

                Comparator comparator;
                if (asc.equals(Boolean.TRUE)) {
                    comparator = Comparator.nullsFirst(Comparator.naturalOrder());
                } else {
                    comparator = Comparator.nullsLast(Comparator.naturalOrder().reversed());
                }
                int compare = comparator.compare(data1, data2);
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        }

        @Override
        public int hashCode() {
            return booleanList != null ? booleanList.hashCode() : 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ListObjectComparator that = (ListObjectComparator) o;

            return Objects.equals(booleanList, that.booleanList);
        }

    }

}
