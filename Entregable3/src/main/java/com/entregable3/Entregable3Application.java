package com.entregable3;

import com.entregable3.utils.CargaDeDatos;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Entregable3Application {

    @Autowired
    private CargaDeDatos cargaDeDatos;

    public static void main(String[] args) {
        SpringApplication.run(Entregable3Application.class, args);
    }

    @PostConstruct
    public void init() throws IOException {
        cargaDeDatos.llenarTablaEstudiante();
        cargaDeDatos.llenarTablaCarrera();
        cargaDeDatos.llenarTablaCarreraEstudiante();
    }

}
