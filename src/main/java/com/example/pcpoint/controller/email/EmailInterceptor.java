package com.example.pcpoint.controller.email;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class EmailInterceptor implements HandlerInterceptor {



    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        String value = request.getParameter("email");

        System.out.println("THIS SHOULD BE THE EMAIL" + value);
    }
}
