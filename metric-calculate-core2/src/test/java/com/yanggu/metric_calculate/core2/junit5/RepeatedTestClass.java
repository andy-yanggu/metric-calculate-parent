package com.yanggu.metric_calculate.core2.junit5;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

/**
 * 重复测试
 */
class RepeatedTestClass {

    @DisplayName("重复测试")
    @RepeatedTest(value = 3)
    void repeatedTest1() {
        System.out.println("执行测试");
    }

    @DisplayName("自定义名称重复测试")
    @RepeatedTest(value = 3, name = "{displayName} 第 {currentRepetition} 次")
    void repeatedTest2() {
        System.out.println("执行测试");
    }

}
