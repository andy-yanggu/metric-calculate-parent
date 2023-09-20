package com.yanggu.metric_calculate.java_new_feature;

/**
 * JAVA9 新增私有接口方法（private interface method）
 */
public interface MyInterface {
    //公共抽象方法
    void publicMethod();

    /**
     * 默认方法
     * <p>只允许实例和接口内部其他默认方法调用</p>
     */
    default void defaultMethod1() {
        // 调用私有接口方法
        privateMethod();
        System.out.println("This is a default method.");
    }

    // 私有接口方法
    private void privateMethod() {
        System.out.println("This is a private method.");
    }

}
