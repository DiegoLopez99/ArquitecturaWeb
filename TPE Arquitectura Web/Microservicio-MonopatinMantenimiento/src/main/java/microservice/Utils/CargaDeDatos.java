package microservice.Utils;

import microservice.Model.Mantenimiento;
import microservice.Model.Monopatin;
import microservice.Service.MantenimientoService;
import microservice.Service.MonopatinService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class CargaDeDatos {
    private final MantenimientoService mantenimientoService;
    private final MonopatinService monopatinService;
    @Autowired
    public CargaDeDatos(MantenimientoService mantenimientoService, MonopatinService monopatinService) {
        this.mantenimientoService = mantenimientoService;
        this.monopatinService = monopatinService;
    }

    public void llenarTablaMonopatin() throws IOException{
        CSVParser datosMonopatin= CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/Monopatin.csv"));
        for (CSVRecord row : datosMonopatin){
            String estado = row.get("estado");
            String ubicacion = row.get("ubicacion");
            float km_recorridos = Integer.parseInt(row.get("km_recorridos"));
            Monopatin est = new Monopatin(estado,ubicacion,km_recorridos);
            monopatinService.guardarMonopatin(est);
        }
    }

    public void llenarTablaMantenimiento() throws IOException{
        CSVParser datosMantenimiento= CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/Mantenimiento.csv"));
        for (CSVRecord row : datosMantenimiento){
            LocalDate inicio = LocalDate.parse(row.get("inicio"));
            LocalDate finalizacion = LocalDate.parse(row.get("finalizacion"));
            String motivo = row.get("motivo");
            Long id_monopatin = Long.valueOf(row.get("id_monopatin"));
            Boolean reparado = Boolean.valueOf(row.get("reparado"));
            Monopatin monopatin = monopatinService.obtenerMonopatinPorId(id_monopatin);
            Mantenimiento est = new Mantenimiento(inicio,finalizacion,motivo,monopatin,reparado);
            mantenimientoService.guardarMantenimiento(est);
        }
    }

}
