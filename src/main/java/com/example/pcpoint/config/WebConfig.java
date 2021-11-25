package com.example.pcpoint.config;

import com.example.pcpoint.controller.interceptor.LoggerInterceptor;
import com.example.pcpoint.controller.interceptor.StatsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StatsInterceptor statsInterceptor;
    private final LoggerInterceptor loggerInterceptor;

    public WebConfig(StatsInterceptor statsInterceptor, LoggerInterceptor loggerInterceptor) {
        this.statsInterceptor = statsInterceptor;
        this.loggerInterceptor = loggerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statsInterceptor);
        registry.addInterceptor(loggerInterceptor);
    }
}
