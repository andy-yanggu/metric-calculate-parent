package com.yanggu.metric_calculate.core2.test;

public class MyClass implements MyInterface {
    @Override
    public void publicMethod() {
        System.out.println("This is a public method.");
    }

    @Override
    public void defaultMethod1() {
        MyInterface.super.defaultMethod1();
    }

    public static void main(String[] args) {
        MyClass myObj = new MyClass();
        myObj.publicMethod();   // 调用公共抽象方法
        myObj.defaultMethod1();  // 调用默认方法
    }
}