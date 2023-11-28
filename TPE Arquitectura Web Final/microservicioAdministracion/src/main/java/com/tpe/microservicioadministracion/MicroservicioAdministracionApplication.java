package com.tpe.microservicioadministracion;

import com.tpe.microservicioadministracion.utils.CargaDeDatos;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MicroservicioAdministracionApplication {
    @Autowired
    private CargaDeDatos cargaDeDatos;

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioAdministracionApplication.class, args);
    }

    @PostConstruct
    public void init() throws IOException {
        cargaDeDatos.llenarTablaRol();
        cargaDeDatos.llenarTablaAministradores();
        cargaDeDatos.llenarTablaTarifas();
    }
}
