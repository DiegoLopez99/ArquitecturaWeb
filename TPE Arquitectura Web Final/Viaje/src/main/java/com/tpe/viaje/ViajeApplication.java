package com.tpe.viaje;

import com.tpe.viaje.util.CargaDeDatos;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.ParseException;

@SpringBootApplication
public class ViajeApplication {

	@Autowired
	private CargaDeDatos cargaDeDatos;

	public static void main(String[] args) {
		SpringApplication.run(ViajeApplication.class, args);
	}

	@PostConstruct
	public void init() throws IOException, ParseException {
		cargaDeDatos.llenarTablaViajes();
		cargaDeDatos.llenarTablaPausa();
	}
}
