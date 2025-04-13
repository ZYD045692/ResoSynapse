package org.amm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // 如果保留Filter/Listener需要此注解
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}