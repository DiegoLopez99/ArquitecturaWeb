package microservice;

import jakarta.annotation.PostConstruct;


import microservice.Utils.CargaDeDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;

@SpringBootApplication
public class MonopatinMantenimiento {

    @Autowired
    private CargaDeDatos cargaDeDatos;

    public static void main(String[] args) {
        SpringApplication.run(MonopatinMantenimiento.class, args);
    }


    @PostConstruct
    public void init() throws IOException {
        cargaDeDatos.llenarTablaMonopatin();
        cargaDeDatos.llenarTablaMantenimiento();
    }

}
