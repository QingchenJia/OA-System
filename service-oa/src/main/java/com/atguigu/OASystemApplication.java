package com.atguigu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class OASystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OASystemApplication.class, args);
        log.info("\napi-url:\thttp://localhost:8080/doc.html");
    }
}
