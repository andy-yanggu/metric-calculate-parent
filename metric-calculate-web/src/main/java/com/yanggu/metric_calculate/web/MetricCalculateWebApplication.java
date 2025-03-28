package com.yanggu.metric_calculate.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 指标计算api服务主启动类
 */
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MetricCalculateWebApplication {

    public static void main(String[] args) {
        //设置彩色日志
        System.setProperty("log4j.skipJansi", "false");
        SpringApplication.run(MetricCalculateWebApplication.class, args);
    }

}
