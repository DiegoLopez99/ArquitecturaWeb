package com.securityjwt.security;

import com.securityjwt.security.Config.SecurityConfiguration;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SecurityApplication {

    @Autowired
    SecurityConfiguration securityConfiguration;
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @PostConstruct
    public void init() throws IOException {
        securityConfiguration.databasePopulator();
    }
}
