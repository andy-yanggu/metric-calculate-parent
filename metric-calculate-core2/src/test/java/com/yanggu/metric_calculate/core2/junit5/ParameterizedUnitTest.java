package com.yanggu.metric_calculate.core2.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JUnit 5 参数化测试
 */
class ParameterizedUnitTest {

    /**
     * ValueSource提供方法入参
     * <p>提供八大基本数据类型、string和class作为方法的入参</p>
     *
     * @param num
     */
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8})
    void testNumberShouldBeEven(int num) {
        Assertions.assertEquals(0, num % 2);
    }

    /**
     * CsvSource提供多个方法的入参
     * <p>通过","分割可以提供多个参数</p>
     *
     * @param id
     * @param name
     */
    @ParameterizedTest
    @CsvSource({"1,One", "2,Two", "3,Three"})
    void testDataFromCsv(long id, String name) {
        System.out.printf("id: %d, name: %s", id, name);
    }

}