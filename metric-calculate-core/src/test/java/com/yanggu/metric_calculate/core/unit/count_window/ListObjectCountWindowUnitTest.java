package com.yanggu.metric_calculate.core.unit.count_window;

import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.count_window.ListObjectCountWindowUnit.Fields;
import org.junit.Test;

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
        param.put(Fields.aggregateType, "SUM");

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();
        param.put(Fields.unitFactory, unitFactory);

        ListObjectCountWindowUnit<CubeLong> countWindowUnit = new ListObjectCountWindowUnit<>(param);
        countWindowUnit.add(CubeLong.of(1L));
        Object value = countWindowUnit.value();
        assertEquals(1L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param).add(CubeLong.of(2L)));
        value = countWindowUnit.value();
        assertEquals(3L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param).add(CubeLong.of(3L)));
        value = countWindowUnit.value();
        assertEquals(6L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param).add(CubeLong.of(4L)));
        value = countWindowUnit.value();
        assertEquals(10L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param).add(CubeLong.of(5L)));
        value = countWindowUnit.value();
        assertEquals(15L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param).add(CubeLong.of(6L)));
        value = countWindowUnit.value();
        assertEquals(20L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<CubeLong>(param).add(CubeLong.of(7L)));
        value = countWindowUnit.value();
        assertEquals(25L, value);
    }

}