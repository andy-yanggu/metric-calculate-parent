package com.yanggu.metric_calculate.test;


import org.dromara.hutool.core.comparator.ComparatorChain;
import org.dromara.hutool.core.comparator.FuncComparator;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparatorChainTest {

    @Test
    void test1() {
        //
        List<JSONObject> fieldOrderParamList = new ArrayList<>();
        JSONObject fieldOrderParam1 = new JSONObject()
                .putValue("asc", true)
                .putValue("name", "username");
        fieldOrderParamList.add(fieldOrderParam1);

        JSONObject fieldOrderParam2 = new JSONObject()
                .putValue("asc", false)
                .putValue("name", "age");
        fieldOrderParamList.add(fieldOrderParam2);

        ComparatorChain<JSONObject> comparatorChain = new ComparatorChain<>();
        for (JSONObject fieldOrderParam : fieldOrderParamList) {
            Boolean asc = fieldOrderParam.getBool("asc");
            Comparator tempComparator;
            Function<JSONObject, Comparable<?>> function =
                    tempJson -> (Comparable<?>) tempJson.getObj(fieldOrderParam.getStr("name"));
            if (asc) {
                tempComparator = new FuncComparator<>(false, false, function);
            } else {
                tempComparator = new FuncComparator<>(true, false, function).reversed();
            }
            comparatorChain.addComparator(tempComparator);
        }
        JSONObject json1 = new JSONObject()
                .putValue("username", "111")
                .putValue("age", 20);
        JSONObject json2 = new JSONObject()
                .putValue("username", "111")
                .putValue("age", 19);

        int compare = comparatorChain.compare(json1, json2);
        assertEquals(-1, compare);

        //升序时, null放在最前面
        json1.putNull("username");
        compare = comparatorChain.compare(json1, json2);
        assertEquals(-1, compare);

        //降序时, null放在最后面
        json1.putValue("username", "111");
        json2.putNull("age");
        compare = comparatorChain.compare(json1, json2);
        assertEquals(1, compare);
    }

}
