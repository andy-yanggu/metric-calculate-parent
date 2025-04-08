package com.yanggu.metric_calculate.test2;


import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.hutool.core.comparator.ComparatorChain;
import org.dromara.hutool.core.comparator.FuncComparator;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 多字段比较单元测试类。实现类似SQL的ORDER BY语句。
 */
@DisplayName("多字段比较单元测试类")
class ComparatorChainTest {

    @Test
    void test1() {
        //定义多字段比较规则
        //username升序，age降序。类似SQL的ORDER BY username ASC, age DESC
        List<FieldOrderParam> fieldOrderParamList = new ArrayList<>();
        FieldOrderParam fieldOrderParam1 = new FieldOrderParam()
                .setAsc(true)
                .setName("username");
        fieldOrderParamList.add(fieldOrderParam1);

        FieldOrderParam fieldOrderParam2 = new FieldOrderParam()
                .setAsc(false)
                .setName("age");
        fieldOrderParamList.add(fieldOrderParam2);

        ComparatorChain<User> comparatorChain = new ComparatorChain<>();
        for (FieldOrderParam fieldOrderParam : fieldOrderParamList) {
            Boolean asc = fieldOrderParam.getAsc();
            Comparator<User> tempComparator;
            //提取出相应字段值的函数
            Function<User, Comparable<?>> function =
                    tempJson -> (Comparable<?>) FieldUtil.getFieldValue(tempJson, fieldOrderParam.getName());
            if (asc) {
                tempComparator = new FuncComparator<>(false, false, function);
            } else {
                tempComparator = new FuncComparator<>(true, false, function).reversed();
            }
            comparatorChain.addComparator(tempComparator);
        }
        User user1 = new User();
        user1.setUsername("111");
        user1.setAge(20);

        User user2 = new User();
        user2.setUsername("111");
        user2.setAge(19);

        int compare = comparatorChain.compare(user1, user2);
        assertEquals(-1, compare);

        //升序时, null放在最前面
        user1.setUsername(null);
        compare = comparatorChain.compare(user1, user2);
        assertEquals(-1, compare);

        //降序时, null放在最后面
        user1.setUsername("111");
        user2.setAge(null);
        compare = comparatorChain.compare(user1, user2);
        assertEquals(1, compare);
    }

    /**
     * 字段排序参数
     */
    @Data
    @Accessors(chain = true)
    private static class FieldOrderParam {

        private Boolean asc;

        private String name;
    }

    @Data
    @Accessors(chain = true)
    private static class User {

        private String username;

        private Integer age;

    }

}
