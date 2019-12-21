package com.cfy.interest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cfy.interest.mapper")    //开启mapper扫描
public class InterestApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterestApplication.class, args);
    }

}
