package com.yanggu.metric_calculate.core2.test;


import cn.hutool.json.JSONObject;
import com.google.common.collect.Ordering;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多字段排序需求
 * <p>输入数据是JSONObject, 按照给定字段对应的值进行升序和降序排序</p>
 * <p>类似SQL中的ORDER BY语义</p>
 * <p>升序时, 字段的值为null放在最前面, 降序时, 字段的值为null放在最后面</p>
 */
public class TestGuavaOrdering {

    @Test
    public void test1() {
        //id升序、age降序
        //升序时, 字段的值为null放在最前面, 降序时, 字段的值为null放在最后面
        //该数据需要在前端进行配置
        List<FieldOrder> fieldOrderList =
                Arrays.asList(new FieldOrder("id", false), new FieldOrder("age", true));

        List<Ordering<JSONObject>> orderingList = new ArrayList<>();
        for (FieldOrder fieldOrder : fieldOrderList) {
            Ordering<@Nullable Comparable<?>> comparableOrdering;
            //降序排序
            if (fieldOrder.getDesc()) {
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


        JSONObject user1 = new JSONObject().set("id", 1).set("age", 20);
        JSONObject user2 = new JSONObject().set("id", 1).set("age", 30);
        JSONObject user3 = new JSONObject().set("id", 2).set("age", null);
        JSONObject user4 = new JSONObject().set("id", 2).set("age", 30);
        JSONObject user5 = new JSONObject().set("id", null).set("age", 30);
        JSONObject user6 = new JSONObject().set("id", null).set("age", null);
        JSONObject user7 = new JSONObject().set("id", 3).set("age", 40);

        List<JSONObject> userList = Arrays.asList(user1, user2, user3, user4, user5, user6, user7);
        userList.sort(userOrdering);

        Assert.assertEquals(Arrays.asList(user5, user6, user2, user1, user4, user3, user7), userList);

    }

    @Data
    @AllArgsConstructor
    public static class FieldOrder {

        /**
         * 字段名
         */
        private String fieldName;

        /**
         * 是否降序
         */
        private Boolean desc;

    }

}
