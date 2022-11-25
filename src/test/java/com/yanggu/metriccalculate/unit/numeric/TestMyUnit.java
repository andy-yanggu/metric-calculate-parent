package com.yanggu.metriccalculate.unit.numeric;

import cn.hutool.core.date.DateUtil;
import com.yanggu.metriccalculate.number.CubeLong;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.HashMap;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestMyUnit {

    @Test
    public void test1() {
        HashMap<String, Object> params = new HashMap<>();

        //一天内3小时内的交易比例0.6, 如果大于0.6就返回1, 否则返回0
        params.put(MyUnit.Fields.ratio, 0.6D);
        params.put(MyUnit.Fields.continueHour, 3);
        long currentTimeMillis = DateUtil.parseDateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:00:00")).getTime();

        MyUnit<CubeLong> myUnit = new MyUnit<>(CubeLong.of(currentTimeMillis), params);
        //只有一条交易, 应该返回1
        assertEquals(1, myUnit.value());

        //新的交易在3小时外, 应该返回0
        myUnit.merge(new MyUnit<>(CubeLong.of(currentTimeMillis + HOURS.toMillis(3L)), params));
        assertEquals(0, myUnit.value());

        //只有2条交易在3小时内 2.0 / 3 = 0.66 > 0.6
        myUnit.merge(new MyUnit<>(CubeLong.of(currentTimeMillis + HOURS.toMillis(2L)), params));
        assertEquals(1, myUnit.value());

        //有3条交易在3小时内, 3.0 / 4 = 0.75 > 0.6
        myUnit.merge(new MyUnit<>(CubeLong.of(currentTimeMillis + HOURS.toMillis(2L) + MINUTES.toMillis(59L)), params));
        assertEquals(1, myUnit.value());
    }

    @Test
    public void test2() throws Exception {
        String classPath1 = "file:///D:/var/test/test1/Test.class";
        URL url1 = new File(classPath1).toURI().toURL();
        URLClassLoader classLoader1 = URLClassLoader.newInstance(new URL[]{url1});
        Class<?> clazz1 = classLoader1.loadClass("com.zetyun.magiccube.sdk.Test");

        String classPath2 = "file:///D:/var/test/test2/Test.class";
        URL url2 = new File(classPath2).toURI().toURL();
        URLClassLoader classLoader2 = URLClassLoader.newInstance(new URL[]{url2});
        Class<?> clazz2 = classLoader2.loadClass("com.zetyun.magiccube.sdk.Test");

        Object o1 = clazz1.newInstance();
        Method test1 = clazz1.getMethod("test");
        test1.invoke(o1);

        Object o2 = clazz2.newInstance();
        Method test2 = clazz2.getMethod("test");
        test2.invoke(o2);

    }

}
