package com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct;

import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 多字段去重
 */
@Data
public class MultiFieldDistinctKey implements Comparable<MultiFieldDistinctKey>, Cloneable2<MultiFieldDistinctKey> {

    private List<Object> fieldList;

    @Override
    public int hashCode() {
        return Objects.hash(fieldList.toArray());
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        MultiFieldDistinctKey thatData = (MultiFieldDistinctKey) that;
        Object[] thisObjects = fieldList.toArray();
        Object[] thatObjects = thatData.fieldList.toArray();
        return Arrays.equals(thisObjects, thatObjects);
    }

    @Override
    public MultiFieldDistinctKey fastClone() {
        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
        multiFieldDistinctKey.setFieldList(this.fieldList);
        return multiFieldDistinctKey;
    }

    @Override
    public int compareTo(MultiFieldDistinctKey that) {
        return 0;
    }

}
