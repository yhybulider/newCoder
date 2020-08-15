package com.whllow.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer(){

        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textProducer.font.size","32");
        properties.setProperty("kaptcha.textProducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textProducer.char.string","0123456789abcdefghijklnmopqrstuvwxyz");
        properties.setProperty("kaptcha.textProducer.char.length","3");
        properties.setProperty("kaptcha.noise.imp","com.google.code.kaptcha.impl.NONoise");



        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
