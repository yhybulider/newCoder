package com.whllow.community.controller.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 定义拦截器的类
@Component
public class AlphaInterceptor implements HandlerInterceptor {
    //定义日志，打日志

    private static final Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);

    //在Controller之前执行 在拦截之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.debug("preHandle  :" + handler.toString());
        return true;//true为将请求传递下去 ,false为拦截请求，现在只是在窗口有打印信息
    }

    //在Controller之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("postHandle:  " + handler.toString());
    }

    //在templateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.debug("afterCompletion: " + handler.toString());
    }
}
