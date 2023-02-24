package com.yanggu.metric_calculate.core.unit.collection;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.UdafCustomParam;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@Collective(retainObject = true)
@MergeType(value = "LISTOBJECT", useParam = true, timeWindowType = TIME_SLIDING_WINDOW)
public class ListObjectUnit<T extends Clone<T>> implements CollectionUnit<T, ListObjectUnit<T>>, Value<List<T>>, Serializable {

    private static final long serialVersionUID = -6955796189929816087L;

    private List<T> values = new ArrayList<>();

    @UdafCustomParam
    private Integer limit = 0;

    public ListObjectUnit(Map<String, Object> param) {
        if (CollUtil.isEmpty(param)) {
            return;
        }
        Object tempLimit = param.get("limit");
        if (tempLimit instanceof Integer) {
            this.limit = (Integer) tempLimit;
        }
    }

    public ListObjectUnit(T value) {
        this();
        add(value);
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * add.
     *
     * @return
     */
    @Override
    public ListObjectUnit<T> add(T value) {
        if (this.limit.equals(0)) {
            this.values.add(value);
            return this;
        }
        if (this.limit > 0 && this.limit > this.values.size()) {
            this.values.add(value);
            return this;
        }
        return this;
    }

    @Override
    public ListObjectUnit<T> merge(ListObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.limit = Math.max(this.limit, that.limit);
        that.values.forEach(this::add);
        return this;
    }

    @Override
    public List<T> value() {
        return this.values;
    }

    @Override
    public ListObjectUnit<T> fastClone() {
        ListObjectUnit<T> mergeableListObject = new ListObjectUnit<>();
        mergeableListObject.limit = this.limit;
        for (T item : values) {
            mergeableListObject.values.add(item.fastClone());
        }
        return mergeableListObject;
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        ListObjectUnit<T> thatUnit = (ListObjectUnit) that;
        if (this.values == null) {
            if (thatUnit.values != null) {
                return false;
            }
        } else if (!this.values.equals(thatUnit.values)) {
            return false;
        }
        return this.limit == thatUnit.limit;
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, list=%s}", this.limit, this.values.toString());
    }

}
