package com.whllow.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
// kaptcha的配置类，用来生成验证码的
@Configuration
public class KaptchaConfig {
// bean自动装配在容器里
    @Bean
    public Producer kaptchaProducer(){
        // properties对象，不用在properties文件配置也可以
        Properties properties = new Properties();
        // 验证图的各个参数和文字大小颜色等
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textProducer.font.size","32");
        properties.setProperty("kaptcha.textProducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textProducer.char.string","0123456789abcdefghijklnmopqrstuvwxyz");
        properties.setProperty("kaptcha.textProducer.char.length","3");
        properties.setProperty("kaptcha.noise.imp","com.google.code.kaptcha.impl.NONoise");

        // 实现类实例化
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
