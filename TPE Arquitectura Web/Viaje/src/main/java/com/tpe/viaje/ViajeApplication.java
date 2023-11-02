package com.tpe.viaje;

import com.tpe.viaje.util.CargaDeDatos;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ViajeApplication {

	@Autowired
	private CargaDeDatos cargaDeDatos;

	public static void main(String[] args) {
		SpringApplication.run(ViajeApplication.class, args);
	}

	@PostConstruct
	public void init() throws IOException {
		cargaDeDatos.llenarTablaViajes();
		cargaDeDatos.llenarTablaPausa();
	}
}
