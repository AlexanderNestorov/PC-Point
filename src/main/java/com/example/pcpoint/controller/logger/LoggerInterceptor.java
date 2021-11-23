package com.example.pcpoint.controller.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler){

        String timeOfRequest =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getSession().getCreationTime())
                        , TimeZone.getDefault().toZoneId())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        log.info("Request: [" + request.getMethod() + "]" +
                " To ["  + request.getRequestURI() + "] At [ " + timeOfRequest + "]");


        return true;
    }

}
