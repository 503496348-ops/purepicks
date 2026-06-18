package com.atomcollide.purepicks;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.atomcollide.purepicks.mapper")
@SpringBootApplication
public class purepicksApplication {
    public static void main(String[] args) {
        SpringApplication.run(purepicksApplication.class, args);
    }
}