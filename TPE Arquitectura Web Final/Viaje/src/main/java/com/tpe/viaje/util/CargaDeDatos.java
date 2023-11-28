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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
            String id = row.get("id");
            long id_monopatin = Long.parseLong(row.get("id_monopatin"));
            String fechaInicio = row.get("inicio");
            String fechaFin =  row.get("fin");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
            LocalDateTime fin =  LocalDateTime.parse(fechaFin, formatter);;
            float km = Float.parseFloat(row.get("km"));
            float costo = Float.parseFloat(row.get("costo"));
            Viaje viaje = new Viaje(id,id_monopatin,inicio,fin,km,costo);
            viajeService.guardarViaje(viaje);
        }
    }

    public void llenarTablaPausa() throws IOException, ParseException {
        CSVParser datosCarreras = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/Pausas.csv"));
        for (CSVRecord row : datosCarreras){
            String id = row.get("id");
            String tiempoString = row.get("inicio");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date tiempo = sdf.parse(tiempoString);
            String motivo = row.get("motivo");
            String id_viaje = row.get("id_viaje");
            Pausa pausa = new Pausa(id, tiempo, motivo, id_viaje);
            pausaService.guardarPausa(pausa);
        }
    }
}
