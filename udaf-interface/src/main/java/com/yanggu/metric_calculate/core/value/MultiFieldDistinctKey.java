package com.yanggu.metric_calculate.core.value;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 多字段去重
 */
@Data
public class MultiFieldDistinctKey {

    private List<String> fieldList;

    private JSONObject inputData;

    @Override
    public int hashCode() {
        Object[] objects = fieldList.stream().map(tempField -> inputData.get(tempField)).toArray();
        return Objects.hash(objects);
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
        Object[] thisObjects = fieldList.stream().map(tempField -> inputData.get(tempField)).toArray();
        Object[] thatObjects = thatData.fieldList.stream().map(tempField -> thatData.inputData.get(tempField)).toArray();
        return Arrays.equals(thisObjects, thatObjects);
    }

}
