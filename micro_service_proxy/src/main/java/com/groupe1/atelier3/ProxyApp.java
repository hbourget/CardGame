package com.groupe1.atelier3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ProxyApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ProxyApp.class, args);
    }
}
