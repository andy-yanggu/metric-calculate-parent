package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import cn.hutool.core.comparator.ComparatorChain;
import cn.hutool.core.comparator.FuncComparator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 多字段排序
 */
@Data
@NoArgsConstructor
public class MultiFieldOrderCompareKey implements Comparable<MultiFieldOrderCompareKey> {

    private List<Object> dataList;

    private ComparatorChain<List<Object>> comparatorChain;

    public MultiFieldOrderCompareKey(List<Object> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int compareTo(@NonNull MultiFieldOrderCompareKey that) {
        if (this.comparatorChain == null) {
            this.comparatorChain = that.comparatorChain;
        }
        return this.comparatorChain.compare(dataList, that.dataList);
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

        return this.dataList.equals(that.dataList);
    }

    @Override
    public int hashCode() {
        return this.dataList.hashCode();
    }

    public static ComparatorChain<List<Object>> getComparatorChain(List<Boolean> booleanList) {
        ComparatorChain<List<Object>> comparatorChain = new ComparatorChain<>();
        for (int i = 0; i < booleanList.size(); i++) {
            Boolean result = booleanList.get(i);
            int finalIndex = i;
            Function<List<Object>, Comparable<?>> function =
                    tempList -> (Comparable<?>) tempList.get(finalIndex);
            Comparator tempComparator;
            //降序排序
            if (Boolean.FALSE.equals(result)) {
                //降序时, null放在最后面
                tempComparator = new FuncComparator<>(true, function).reversed();
            } else {
                //升序排序, null放在最前面
                tempComparator = new FuncComparator<>(false, function);
            }
            comparatorChain.addComparator(tempComparator);
        }
        return comparatorChain;
    }

}
