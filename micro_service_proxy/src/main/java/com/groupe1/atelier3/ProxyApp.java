package com.groupe1.atelier3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

@SpringBootApplication
public class ProxyApp extends SpringBootServletInitializer {
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    public static void main(String[] args) {
        SpringApplication.run(ProxyApp.class, args);
    }
    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfigurationSource);
    }
}
