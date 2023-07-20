package com.yanggu.metric_calculate.core2.junit5;

import org.junit.jupiter.api.*;

/**
 * junit5学习
 * <p>junit5不再需要单元测试类必须是public修饰的</p>
 */
@DisplayName("我的第一个测试用例")
class MyFirstTestCaseTest {

    /**
     * 在所有单元测试前执行一次, 且方法必须是static修饰
     */
    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");
    }

    /**
     * 在所有单元测试后执行一次, 且方法必须是static修饰
     */
    @AfterAll
    public static void cleanup() {
        System.out.println("清理数据");
    }

    /**
     * 每个单元测试执行前, 都会执行一次
     */
    @BeforeEach
    public void setUp() {
        System.out.println("当前测试方法开始");
    }

    /**
     * 每个单元测试执行完成后, 都会执行一次
     */
    @AfterEach
    public void shutDown() {
        System.out.println("当前测试方法结束");
    }

    /**
     * DisplayName标识单元测试的名称
     * junit5不再要求单元测试方法必须用public修饰
     */
    @DisplayName("我的第一个测试")
    @Test
    void testFirstTest() {
        System.out.println("我的第一个测试开始测试");
    }

    @DisplayName("我的第二个测试")
    @Test
    void testSecondTest() {
        System.out.println("我的第二个测试开始测试");
    }

    /**
     * Disabled注解可以跳过该单元测试方法, 最好在注解中写跳过原因
     * <p>Disable可以作用在类和方法上, 用于跳过类或者方法</p>
     */
    @DisplayName("我的第三个测试")
    @Disabled("测试Disable注解")
    @Test
    void testThirdTest() {
        System.out.println("我的第三个测试开始测试");
    }

}