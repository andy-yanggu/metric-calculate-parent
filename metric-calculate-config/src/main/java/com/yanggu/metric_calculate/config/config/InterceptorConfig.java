package com.yanggu.metric_calculate.config.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 登录拦截器配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.login-interceptor")
public class InterceptorConfig implements WebMvcConfigurer {

    private List<String> excludePathList;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry添加拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathList)
                .order(1);
    }

}