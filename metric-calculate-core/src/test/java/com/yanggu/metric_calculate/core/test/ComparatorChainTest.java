package com.yanggu.metric_calculate.core.test;


import cn.hutool.core.comparator.ComparatorChain;
import cn.hutool.core.comparator.FuncComparator;
import cn.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparatorChainTest {

    @Test
    void test1() {
        List<JSONObject> fieldOrderParamList = new ArrayList<>();
        JSONObject fieldOrderParam1 = new JSONObject()
                .set("asc", true)
                .set("name", "username");
        fieldOrderParamList.add(fieldOrderParam1);

        JSONObject fieldOrderParam2 = new JSONObject()
                .set("asc", false)
                .set("name", "age");
        fieldOrderParamList.add(fieldOrderParam2);

        ComparatorChain<JSONObject> comparatorChain = new ComparatorChain<>();
        for (JSONObject fieldOrderParam : fieldOrderParamList) {
            Boolean asc = fieldOrderParam.getBool("asc");
            Comparator tempComparator;
            Function<JSONObject, Comparable<?>> function =
                    tempJson -> (Comparable<?>) tempJson.get(fieldOrderParam.getStr("name"));
            if (asc) {
                tempComparator = new FuncComparator<>(false, function);
            } else {
                tempComparator = new FuncComparator<>(true, function).reversed();
            }
            comparatorChain.addComparator(tempComparator);
        }
        JSONObject json1 = new JSONObject()
                .set("username", "111")
                .set("age", 20);
        JSONObject json2 = new JSONObject()
                .set("username", "111")
                .set("age", 19);

        int compare = comparatorChain.compare(json1, json2);
        assertEquals(-1, compare);

        //升序时, null放在最前面
        json1.set("username", null);
        compare = comparatorChain.compare(json1, json2);
        assertEquals(-1, compare);

        //降序时, null放在最后面
        json1.set("username", "111");
        json2.set("age", null);
        compare = comparatorChain.compare(json1, json2);
        assertEquals(1, compare);
    }

}
