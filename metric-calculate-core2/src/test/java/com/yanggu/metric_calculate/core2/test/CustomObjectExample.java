package com.yanggu.metric_calculate.core2.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.fury.Fury;
import io.fury.Language;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomObjectExample {

    public static void main(String[] args) {
        // Fury应该在多个对象序列化之间复用，不要每次创建新的Fury实例
        Fury fury = Fury.builder().withLanguage(Language.JAVA)
                .withRefTracking(false)
                .requireClassRegistration(false)
                .build();
        byte[] bytes = fury.serialize(createObject());
        System.out.println(fury.deserialize(bytes));
    }

    public static class SomeClass1 {
        Object f1;
        Map<Byte, Integer> f2;
    }

    public static class SomeClass2 {
        Object f1;
        String f2;
        List<Object> f3;
        Map<Byte, Integer> f4;
        Byte f5;
        Short f6;
        Integer f7;
        Long f8;
        Float f9;
        Double f10;
        short[] f11;
        List<Short> f12;
    }

    public static Object createObject() {
        SomeClass1 obj1 = new SomeClass1();
        obj1.f1 = true;
        obj1.f2 = ImmutableMap.of((byte) -1, 2);
        SomeClass2 obj = new SomeClass2();
        obj.f1 = obj1;
        obj.f2 = "abc";
        obj.f3 = Arrays.asList("abc", "abc");
        obj.f4 = ImmutableMap.of((byte) 1, 2);
        obj.f5 = Byte.MAX_VALUE;
        obj.f6 = Short.MAX_VALUE;
        obj.f7 = Integer.MAX_VALUE;
        obj.f8 = Long.MAX_VALUE;
        obj.f9 = 1.0f / 2;
        obj.f10 = 1 / 3.0;
        obj.f11 = new short[]{(short) 1, (short) 2};
        obj.f12 = ImmutableList.of((short) -1, (short) 4);
        return obj;
    }

}