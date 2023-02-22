package com.yanggu.metric_calculate.core.unit.collection;


import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@Data
@Collective(retainObject = false)
@MergeType(value = "LISTFIELD", useParam = true, timeWindowType = TIME_SLIDING_WINDOW)
public class ListFieldUnit<T extends Cloneable2<T>> implements CollectionUnit<T, ListFieldUnit<T>>, Value<List<T>> {

    private ListObjectUnit<T> listObjectUnit;

    public ListFieldUnit() {
        this.listObjectUnit = new ListObjectUnit<>();
    }

    public ListFieldUnit(Map<String, Object> param) {
        this.listObjectUnit = new ListObjectUnit<>(param);
    }

    public ListFieldUnit(T value) {
        this();
        add(value);
    }

    @Override
    public ListFieldUnit<T> add(T value) {
        this.listObjectUnit.add(value);
        return this;
    }

    @Override
    public ListFieldUnit<T> merge(ListFieldUnit<T> that) {
        this.listObjectUnit.merge(that.listObjectUnit);
        return this;
    }

    @Override
    public ListFieldUnit<T> fastClone() {
        ListFieldUnit<T> mergeableListObject = new ListFieldUnit<>();
        mergeableListObject.listObjectUnit = this.listObjectUnit.fastClone();
        return mergeableListObject;
    }

    @Override
    public List<T> value() {
        return this.listObjectUnit.value();
    }

    @Override
    public int hashCode() {
        return listObjectUnit != null ? listObjectUnit.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListFieldUnit<?> that = (ListFieldUnit<?>) o;

        return Objects.equals(listObjectUnit, that.listObjectUnit);
    }

    public List<T> getValues() {
        return this.listObjectUnit.getValues();
    }

    public Integer getLimit() {
        return this.listObjectUnit.getLimit();
    }

}
