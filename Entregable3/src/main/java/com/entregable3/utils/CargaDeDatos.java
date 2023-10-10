package com.entregable3.utils;

import com.entregable3.domain.Carrera;
import com.entregable3.domain.Estudiante;
import com.entregable3.service.CarreraEstudianteService;
import com.entregable3.service.CarreraService;
import com.entregable3.service.EstudianteService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.FileReader;
import java.io.IOException;

@Component
public class CargaDeDatos {
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final CarreraEstudianteService carreraEstudianteService;

    @Autowired
    public CargaDeDatos(EstudianteService estudianteService, CarreraService carreraService, CarreraEstudianteService carreraEstudianteService){
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
        this.carreraEstudianteService = carreraEstudianteService;
    }

    public void llenarTablaEstudiante() throws IOException {
        CSVParser datosEstudiantes = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/estudiantes.csv"));
        for (CSVRecord row : datosEstudiantes){
            int dni = Integer.parseInt(row.get("DNI"));
            String nombre = row.get("nombre");
            String apellido = row.get("apellido");
            int edad = Integer.parseInt(row.get("edad"));
            String genero = row.get("genero");
            String ciudad = row.get("ciudad");
            int nroLibreta = Integer.parseInt(row.get("LU"));
            Estudiante est = new Estudiante(nombre, apellido, edad, genero, dni, ciudad, nroLibreta);
            estudianteService.guardarEstudiante(est);
        }
    }

    public void llenarTablaCarrera() throws IOException {
        CSVParser datosCarreras = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/carreras.csv"));
        for (CSVRecord row : datosCarreras){
            String nombre = row.get("carrera");
            int duracion = Integer.parseInt(row.get("duracion"));
            Carrera carrera = new Carrera(nombre, duracion);
            carreraService.guardarCarrera(carrera);
        }
    }

    public void llenarTablaCarreraEstudiante() throws IOException {
        CSVParser datosCarrerasEstudiantes = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/estudianteCarrera.csv"));
        for (CSVRecord row : datosCarrerasEstudiantes) {
            int idEstudiante = Integer.parseInt(row.get("id_estudiante"));
            long idCarrera = Integer.parseInt(row.get("id_carrera"));
            String inscripcion = row.get("inscripcion");
            String graduacion = row.get("graduacion");
            int antiguedad = Integer.parseInt(row.get("antiguedad"));
            //se busca el estudiante y la carrera
            Estudiante e = estudianteService.obtenerEstudianteDni(idEstudiante);
            Carrera c = carreraService.obtenerCarreraId(idCarrera);

            carreraEstudianteService.matricularEstudianteEnCarrera(e, c, inscripcion, graduacion,antiguedad);
        }
    }
}
