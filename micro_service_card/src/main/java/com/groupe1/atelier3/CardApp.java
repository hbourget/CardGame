package com.groupe1.atelier3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication

public class CardApp {

    public static void main(String[] args) {
        SpringApplication.run(CardApp.class, args);
    }

}
