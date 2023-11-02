package com.tpe.microserviciousarios;

import com.tpe.microserviciousarios.utils.CargaDeDatos;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MicroservicioUsariosApplication {
    @Autowired
    private CargaDeDatos cargaDeDatos;

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioUsariosApplication.class, args);
    }

    @PostConstruct
    public void init() throws IOException {
        cargaDeDatos.llenarTablaCuentas();
        cargaDeDatos.llenarTablaUsuarios();
    }

}
