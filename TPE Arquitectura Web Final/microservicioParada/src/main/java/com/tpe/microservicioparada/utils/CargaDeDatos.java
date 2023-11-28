package com.tpe.microservicioparada.utils;

import com.tpe.microservicioparada.domain.Parada;
import com.tpe.microservicioparada.service.ParadaService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
@Component
public class CargaDeDatos {
    @Autowired
    private final ParadaService paradaService;

    public CargaDeDatos(ParadaService paradaService) {
        this.paradaService = paradaService;
    }

    public void llenarTablaParada() throws IOException {
        CSVParser datosParada = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/paradas.csv"));
        for (CSVRecord row : datosParada){
            String longitud = row.get("longitud");
            String latitud = row.get("latitud");
            Integer capacidad = Integer.parseInt(row.get("capacidad"));
            Parada parada = new Parada(longitud, latitud, capacidad);
            paradaService.guardarParada(parada);
        }
    }
}
