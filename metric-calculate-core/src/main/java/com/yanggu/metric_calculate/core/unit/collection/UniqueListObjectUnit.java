package com.yanggu.metric_calculate.core.unit.collection;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.KeyValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@MergeType(value = "DISTINCTLISTOBJECT", useParam = true)
@Collective(useDistinctField = true, retainObject = true)
public class UniqueListObjectUnit<T extends Cloneable2<T>> implements CollectionUnit<T, UniqueListObjectUnit<T>>,
        Value<Set<Object>> {

    protected Set<T> original = new HashSet<>();

    protected int limit = 0;

    /**
     * 是否只展示value, 不展示key
     */
    private boolean onlyShowValue = true;

    public UniqueListObjectUnit(Map<String, Object> param) {
        if (CollUtil.isEmpty(param)) {
            return;
        }
        Object tempShowValue = param.get(SortedListObjectUnit.Fields.onlyShowValue);
        if (tempShowValue instanceof Boolean) {
            this.onlyShowValue = (boolean) tempShowValue;
        }
        Object tempLimit = param.get("limit");
        if (tempLimit instanceof Integer) {
            this.limit = (int) tempLimit;
        }
    }

    public UniqueListObjectUnit(T value) {
        this();
        add(value);
    }

    public UniqueListObjectUnit(Collection<T> values) {
        this();
        addAll(values);
    }

    /**
     * Construct.
     *
     * @param value input
     * @param limit    limitCnt
     */
    public UniqueListObjectUnit(T value, int limit) {
        this();
        add(value);
        this.limit = limit;
    }

    /**
     * Construct.
     */
    public UniqueListObjectUnit(Collection<T> values, int paramCnt) {
        this();
        addAll(values);
        this.limit = paramCnt;
    }

    public Set<T> original() {
        return original;
    }

    public int limit() {
        return limit;
    }

    @Override
    public UniqueListObjectUnit<T> merge(UniqueListObjectUnit<T> that) {
        return internalMergeOp(that, false);
    }

    private UniqueListObjectUnit<T> internalMergeOp(UniqueListObjectUnit<T> that, boolean hasLimit) {
        if (that == null) {
            return this;
        }
        return originalMerge(that.original, that.limit, hasLimit);
    }

    private UniqueListObjectUnit<T> originalMerge(Set<T> original, int limit, boolean hasLimit) {
        this.original.addAll(original);
        if (!hasLimit) {
            this.limit = Math.max(this.limit, limit);
            int i = this.original.size();
            if (this.limit > 0 && i > this.limit) {
                byte b = 0;
                HashSet<T> hashSet = new HashSet<>();
                int j = i - this.limit;
                for (T item : this.original) {
                    if (b++ < j) {
                        continue;
                    }
                    hashSet.add(item);
                }
                this.original = hashSet;
            }
        }
        return this;
    }

    @Override
    public Set<Object> value() {
        if (CollUtil.isEmpty(original)) {
            return Collections.emptySet();
        }
        if (CollUtil.getFirst(original) instanceof KeyValue && onlyShowValue) {
            Set<Object> returnSet = new HashSet<>();
            original.forEach(temp -> {
                Value<?> value = ((KeyValue<?, ?>) temp).getValue();
                if (value != null) {
                    returnSet.add(ValueMapper.value(value));
                }
            });
            return returnSet;
        }
        return ((Set) original);
    }

    public Collection<T> asCollection() {
        return this.original;
    }

    /**
     * Add value to original.
     * @param value input param
     * @return
     */
    @Override
    public UniqueListObjectUnit<T> add(T value) {
        this.original.add(value);
        if (this.limit > 0 && this.original.size() > this.limit) {
            this.original.remove(this.original.iterator().next());
        }
        return this;
    }

    /**
     * Add values to original.
     */
    public void addAll(Collection<T> values) {
        this.original.addAll(values);
        while (this.limit > 0 && this.original.size() > this.limit) {
            this.original.remove(this.original.iterator().next());
        }
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, set=%s}", this.limit, this.original.toString());
    }

    @Override
    public UniqueListObjectUnit<T> fastClone() {
        UniqueListObjectUnit<T> uniqueListObjectUnit = new UniqueListObjectUnit<>();
        uniqueListObjectUnit.limit = this.limit;
        for (T item : getSet()) {
            uniqueListObjectUnit.getSet().add(item.fastClone());
        }
        return uniqueListObjectUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(original, limit);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UniqueListObjectUnit<T> thatUnit = (UniqueListObjectUnit) that;
        if (this.limit != thatUnit.limit) {
            return false;
        }
        if (this.original == null) {
            return thatUnit.original == null;
        } else {
            return this.original.equals(thatUnit.original);
        }
    }

    public Set<T> getSet() {
        return this.original;
    }

    public void setSet(Set<T> distinctHashRs) {
        this.original = distinctHashRs;
    }

}
