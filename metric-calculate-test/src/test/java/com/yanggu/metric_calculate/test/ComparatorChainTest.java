package com.yanggu.metric_calculate.test;


import com.alibaba.fastjson2.JSONObject;
import org.dromara.hutool.core.comparator.ComparatorChain;
import org.dromara.hutool.core.comparator.FuncComparator;
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
        JSONObject fieldOrderParam1 = new JSONObject();
        fieldOrderParam1.put("asc", true);
        fieldOrderParam1.put("name", "username");
        fieldOrderParamList.add(fieldOrderParam1);

        JSONObject fieldOrderParam2 = new JSONObject();
        fieldOrderParam2.put("asc", false);
        fieldOrderParam2.put("name", "age");
        fieldOrderParamList.add(fieldOrderParam2);

        ComparatorChain<JSONObject> comparatorChain = new ComparatorChain<>();
        for (JSONObject fieldOrderParam : fieldOrderParamList) {
            Boolean asc = fieldOrderParam.getBoolean("asc");
            Comparator tempComparator;
            Function<JSONObject, Comparable<?>> function =
                    tempJson -> (Comparable<?>) tempJson.get(fieldOrderParam.getString("name"));
            if (asc) {
                tempComparator = new FuncComparator<>(false, false, function);
            } else {
                tempComparator = new FuncComparator<>(true, false, function).reversed();
            }
            comparatorChain.addComparator(tempComparator);
        }
        JSONObject json1 = new JSONObject();
        json1.put("username", "111");
        json1.put("age", 20);
        JSONObject json2 = new JSONObject();
        json2.put("username", "111");
        json2.put("age", 19);

        int compare = comparatorChain.compare(json1, json2);
        assertEquals(-1, compare);

        // 升序时, null放在最前面
        json1.put("username", null);
        compare = comparatorChain.compare(json1, json2);
        assertEquals(-1, compare);

        // 降序时, null放在最后面
        json1.put("username", "111");
        json2.put("age", null);
        compare = comparatorChain.compare(json1, json2);
        assertEquals(1, compare);
    }

}
