package com.yanggu.metric_calculate.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yanggu.metric_calculate.config.mapper")
public class MetricCalculateConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricCalculateConfigApplication.class, args);
    }

}
