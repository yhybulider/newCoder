package com.whllow.community.config;

import com.whllow.community.annotation.LoginRequired;
import com.whllow.community.controller.interceptor.AlphaInterceptor;
import com.whllow.community.controller.interceptor.LoginRequiredInterceptor;
import com.whllow.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// 开启注解
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
// 配置拦截器，将其注入，应用在注册和登录环节
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
         .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg",
                 "/**/*.jpeg")
          .addPathPatterns("/register","/login")//拦截注册和登录的功能
        ;
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg",
                        "/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)//是下面的静态资源就不拦截
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg",
                        "/**/*.jpeg");

    }
}
