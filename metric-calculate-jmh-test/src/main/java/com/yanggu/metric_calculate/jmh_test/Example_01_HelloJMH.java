package com.yanggu.metric_calculate.jmh_test;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Example_01_HelloJMH {

    @Benchmark
    public String sayHello() {
        return "HELLO JMH!";
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Example_01_HelloJMH.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }
}