package com.projects.order_create;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.projects.*"})
public class MainApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MainApp.class).run(args);
    }
}
