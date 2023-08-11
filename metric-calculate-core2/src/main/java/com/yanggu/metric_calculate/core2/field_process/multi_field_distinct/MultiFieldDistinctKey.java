package com.yanggu.metric_calculate.core2.field_process.multi_field_distinct;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 多字段去重列表
 */
@Data
@NoArgsConstructor
public class MultiFieldDistinctKey implements Comparable<MultiFieldDistinctKey> {

    private List<Object> fieldList;

    public MultiFieldDistinctKey(List<Object> fieldList) {
        this.fieldList = fieldList;
    }

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
    public String toString() {
        return JSONUtil.toJsonStr(fieldList);
    }

    @Override
    public int compareTo(MultiFieldDistinctKey that) {
        return 0;
    }

}
