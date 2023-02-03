package com.yanggu.metric_calculate.core.unit.collection;


import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

import java.util.Map;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@MergeType(value = "LISTFIELD", useParam = true, timeWindowType = TIME_SLIDING_WINDOW)
@Collective(useCompareField = false, retainObject = false)
public class ListFieldUnit<T extends Cloneable2<T>> extends ListObjectUnit<T> {

    public ListFieldUnit(T value) {
        add(value);
    }

    public ListFieldUnit(Map<String, Object> param) {
        super(param);
    }

    @Override
    public ListFieldUnit<T> fastClone() {
        ListFieldUnit<T> mergeableListObject = new ListFieldUnit<>();
        mergeableListObject.setLimit(getLimit());
        for (T item : getValues()) {
            mergeableListObject.getValues().add(item.fastClone());
        }
        return mergeableListObject;
    }

}
