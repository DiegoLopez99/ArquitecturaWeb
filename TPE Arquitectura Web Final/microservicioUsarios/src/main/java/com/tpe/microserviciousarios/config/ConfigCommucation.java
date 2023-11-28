package com.tpe.microserviciousarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigCommucation {

    @Bean("clienteRest")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
