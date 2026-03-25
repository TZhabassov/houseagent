package com.example.houseagent.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.houseagent")
public class HouseAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseAgentApplication.class, args);
    }
}
