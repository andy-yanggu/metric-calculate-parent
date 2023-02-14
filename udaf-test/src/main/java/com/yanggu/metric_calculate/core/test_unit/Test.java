//package com.yanggu.metric_calculate.core.test_unit;
//
//import com.yanggu.metric_calculate.core.number.CubeLong;
//import com.yanggu.metric_calculate.core.value.KeyValue;
//import com.yanggu.metric_calculate.core.value.MultiFieldCompareKey;
//import com.yanggu.metric_calculate.core.value.MultiFieldDistinctKey;
//
//
//public class Test {
//
//    public static void main(String[] args) {
//        MultiFieldCompareKey multiFieldCompareKey = new MultiFieldCompareKey();
//        KeyValue<MultiFieldCompareKey, CubeLong> kvKeyValue = new KeyValue<>(multiFieldCompareKey, CubeLong.of(1L));
//        System.out.println(kvKeyValue);
//
//        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
//        KeyValue<MultiFieldDistinctKey, CubeLong> keyValue = new KeyValue<>(multiFieldDistinctKey, CubeLong.of(1L));
//        System.out.println(keyValue);
//
//    }
//
//}
