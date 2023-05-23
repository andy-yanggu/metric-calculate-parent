package com.yanggu.metric_calculate;

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
public class MetricCalculateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricCalculateApplication.class, args);
    }

}
