package com.example.pcpoint.config;

import com.example.pcpoint.controller.email.EmailInterceptor;
import com.example.pcpoint.controller.stats.StatsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StatsInterceptor statsInterceptor;
    private final EmailInterceptor emailInterceptor;

    public WebConfig(StatsInterceptor statsInterceptor, EmailInterceptor emailInterceptor) {
        this.statsInterceptor = statsInterceptor;
        this.emailInterceptor = emailInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statsInterceptor);
        registry.addInterceptor(emailInterceptor).addPathPatterns("/api/auth/register");
    }
}
