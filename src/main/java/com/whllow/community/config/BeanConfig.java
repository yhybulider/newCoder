package com.whllow.community.config;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
@Configuration
public class BeanConfig {

    @Bean
    public SimpleDateFormat simpleDateFormat(){  return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    }
}
