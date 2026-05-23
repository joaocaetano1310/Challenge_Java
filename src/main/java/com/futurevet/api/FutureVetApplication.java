package com.futurevet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FutureVetApplication {
    public static void main(String[] args) {
        SpringApplication.run(FutureVetApplication.class, args);
    }
}
