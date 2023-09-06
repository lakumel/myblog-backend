package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

//@Profile("prod")
//@PropertySource(value = "file:${user.home}/myblog/config/application-mysql.yml"
//        , factory = YamlPropertySourceFactory.class
//        , ignoreResourceNotFound = true)
public class DBConfigYml {
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.hikari.username}")
//    private String username;
//
//    @Value("${spring.datasource.hikari.password}")
//    private String password;
//
//    @Bean
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .driverClassName(driverClassName)
//                .url(url)
//                .username(username)
//                .password(password)
//                .build();
//    }
}
