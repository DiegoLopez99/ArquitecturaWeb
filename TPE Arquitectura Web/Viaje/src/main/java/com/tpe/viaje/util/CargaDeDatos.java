package com.tpe.viaje.util;


import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import com.tpe.viaje.service.PausaService;
import com.tpe.viaje.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class CargaDeDatos {
    private final ViajeService viajeService;
    private final PausaService pausaService;


    @Autowired
    public CargaDeDatos(ViajeService viajeService, PausaService pausaService){
        this.viajeService = viajeService;
        this.pausaService = pausaService;
    }

    public void llenarTablaViajes() throws IOException {
        CSVParser datosViajes = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/Viajes.csv"));
        for (CSVRecord row : datosViajes){
            long id = Long.parseLong(row.get("id"));
            long id_monopatin = Long.parseLong(row.get("id_monopatin"));
            LocalDateTime inicio = LocalDateTime.parse(row.get("inicio"));
            LocalDateTime fin =  LocalDateTime.parse(row.get("fin"));
            float km = Float.parseFloat(row.get("km"));
            float costo = Float.parseFloat(row.get("costo"));
            Viaje viaje = new Viaje(id,id_monopatin,inicio,fin,km,costo);
            viajeService.guardarViaje(viaje);
        }
    }

    public void llenarTablaPausa() throws IOException {
        CSVParser datosCarreras = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/Pausas.csv"));
        for (CSVRecord row : datosCarreras){
            long id = Long.parseLong(row.get("id"));
            Time inicio = Time.valueOf(row.get("inicio"));
            String motivo = row.get("motivo");
            long id_viaje = Long.parseLong(row.get("id_viaje"));
            Viaje viaje = viajeService.obtenerViajePorId(id_viaje);
            Pausa pausa = new Pausa(id,inicio,motivo);
            pausa.setViaje(viaje);
            pausaService.guardarPausa(pausa);
        }
    }
}
