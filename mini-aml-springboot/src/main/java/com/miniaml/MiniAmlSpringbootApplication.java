package com.miniaml;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.miniaml.mapper")
public class MiniAmlSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniAmlSpringbootApplication.class, args);
    }

}
