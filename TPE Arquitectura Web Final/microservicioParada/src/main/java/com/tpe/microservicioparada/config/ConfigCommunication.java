package com.tpe.microservicioparada.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigCommunication {
    @Bean("clienteRest")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
