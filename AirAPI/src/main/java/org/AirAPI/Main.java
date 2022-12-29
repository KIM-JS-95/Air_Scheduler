package org.AirAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.AirAPI"})
@ComponentScan(basePackages = {"org.AirAPI.config"})
//@EnableJpaRepositories(basePackages = {"org.AirAPI.Repository"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}