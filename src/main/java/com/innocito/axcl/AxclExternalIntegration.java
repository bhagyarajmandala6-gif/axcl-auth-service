package com.innocito.axcl;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.TimeZone;

@SpringBootApplication
public class AxclExternalIntegration extends SpringBootServletInitializer {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(AxclExternalIntegration.class, args);
    }
}
