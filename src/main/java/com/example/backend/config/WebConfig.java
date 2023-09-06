package com.example.backend.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class WebConfig {

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");  //메시지 리소스 번들 지정
        messageSource.setDefaultEncoding("UTF-8");  //문자 집합 설정 - 한글 깨짐 방지

        return messageSource;
    }
}
