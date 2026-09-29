package com.osoterra.ososense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OsosenseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsosenseBackendApplication.class, args);
    }

}
