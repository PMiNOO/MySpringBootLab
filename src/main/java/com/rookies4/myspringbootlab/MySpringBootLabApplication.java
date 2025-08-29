package com.rookies4.myspringbootlab;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MySpringBootLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootLabApplication.class, args);
    }
    @Bean
    Hibernate6Module hibernate6Module() {
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING,true);
        return hibernate6Module;
    }
}