package com.rookies4.myspringbootlab.config;

import com.rookies4.myspringbootlab.config.vo.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfig {
    @Bean
    public MyEnvironment customVO() {
        return  MyEnvironment.builder()//CustomerVOBuilder
                .mode("테스트모드")
                .rate(0.5)
                .build();
    }
}