package com.yanggu.metric_calculate.core.value;

import cn.hutool.json.JSONObject;
import com.google.common.collect.Ordering;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 多字段排序
 */
@Data
public class MultiFieldCompareKey implements Comparable<MultiFieldCompareKey>, Cloneable2<MultiFieldCompareKey> {

    private List<FieldOrder> fieldOrderList;

    private JSONObject inputData;

    @Override
    public int compareTo(@NonNull MultiFieldCompareKey that) {
        List<Ordering<JSONObject>> orderingList = new ArrayList<>();
        for (FieldOrder fieldOrder : fieldOrderList) {
            Ordering<Comparable<?>> comparableOrdering;
            //降序排序
            if (Boolean.TRUE.equals(fieldOrder.getDesc())) {
                //降序时, null放在最后面
                comparableOrdering = Ordering.natural().reverse().nullsLast();
            } else {
                //升序排序, null放在最前面
                comparableOrdering = Ordering.natural().nullsFirst();
            }
            Ordering<JSONObject> ordering = comparableOrdering.onResultOf(input -> (Comparable<?>) input.get(fieldOrder.getFieldName()));
            orderingList.add(ordering);
        }

        //合并多个比较器
        Ordering<JSONObject> userOrdering = Ordering.compound(orderingList);
        return userOrdering.compare(inputData, that.inputData);
    }

    @Override
    public MultiFieldCompareKey fastClone() {
        MultiFieldCompareKey multiFieldCompareKey = new MultiFieldCompareKey();
        multiFieldCompareKey.setInputData(this.inputData);
        multiFieldCompareKey.setFieldOrderList(this.fieldOrderList);
        return multiFieldCompareKey;
    }

}
