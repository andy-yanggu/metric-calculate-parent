package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.hutool.json.JSONUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 多字段列表
 */
@Data
@NoArgsConstructor
public class MultiFieldData {

    private List<Object> fieldList;

    public MultiFieldData(List<Object> fieldList) {
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
        MultiFieldData thatData = (MultiFieldData) that;
        Object[] thisObjects = fieldList.toArray();
        Object[] thatObjects = thatData.fieldList.toArray();
        return Arrays.equals(thisObjects, thatObjects);
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(fieldList);
    }

}
