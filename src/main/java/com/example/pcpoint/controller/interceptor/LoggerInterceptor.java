package com.example.pcpoint.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
                " To ["  + request.getRequestURI() + "] At [" + timeOfRequest + "]");

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);


        return true;
    }

    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView)
    {

        long startTime = (Long) request.getAttribute("startTime");

        long endTime = System.currentTimeMillis();

        long executeTime = endTime - startTime;

        log.info("Request: [" + request.getMethod() + "]" +
                " To ["  + request.getRequestURI() + "] Executed For [" + executeTime + "] milliseconds");
    }
}
