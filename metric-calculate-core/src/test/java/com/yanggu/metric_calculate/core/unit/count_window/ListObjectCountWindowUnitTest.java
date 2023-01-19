package com.yanggu.metric_calculate.core.unit.count_window;

import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.unit.count_window.ListObjectCountWindowUnit.Fields;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 计数滑动窗口测试类
 */
public class ListObjectCountWindowUnitTest {

    @Test
    public void value() throws Exception {

        //最近5条数据的求和
        Map<String, Object> param = new HashMap<>();
        param.put(Fields.limit, 5);

        ListObjectCountWindowUnit<CubeLong> countWindowUnit = new ListObjectCountWindowUnit<>(param);
        countWindowUnit.add(CubeLong.of(1L));
        Object value = countWindowUnit.value();
        assertEquals(Collections.singletonList(1L), value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param)
                .add(CubeLong.of(2L)));
        value = countWindowUnit.value();
        assertEquals(Arrays.asList(1L, 2L), value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param)
                .add(CubeLong.of(3L)));
        value = countWindowUnit.value();
        assertEquals(Arrays.asList(1L, 2L, 3L), value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param)
                .add(CubeLong.of(4L)));
        value = countWindowUnit.value();
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L), value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param)
                .add(CubeLong.of(5L)));
        value = countWindowUnit.value();
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L, 5L), value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param)
                .add(CubeLong.of(6L)));
        value = countWindowUnit.value();
        assertEquals(Arrays.asList(2L, 3L, 4L, 5L, 6L), value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param)
                .add(CubeLong.of(7L)));
        value = countWindowUnit.value();
        assertEquals(Arrays.asList(3L, 4L, 5L, 6L, 7L), value);
    }

}