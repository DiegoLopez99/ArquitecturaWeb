package org.entregable2;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.entregable2.dto.CarreraInscriptosDTO;
import org.entregable2.dto.EstudiantesPorCarreraPorCiudadDTO;
import org.entregable2.dto.ReporteCarrerasDTO;
import org.entregable2.entity.Carrera;
import org.entregable2.entity.Carrera_Estudiante;
import org.entregable2.entity.Carrera_EstudiantePK;
import org.entregable2.entity.Estudiante;
import org.entregable2.factory.RepositoryFactory;
import org.entregable2.repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    private static CarreraRepository carreraRepository;
    private static EstudianteRepository estudianteRepository;
    private static CarreraEstudianteRepository carreraEstudianteRepository;

    public static void main(String[] args) throws IOException, SQLException {
        CSVParser datosEstudiantes = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/estudiantes.csv"));
        CSVParser datosCarreras = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/carreras.csv"));
        CSVParser datosCarrerasEstudiantes = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/estudianteCarrera.csv"));

        RepositoryFactory repositoryFactory = RepositoryFactory.getEntityManagerFactory(RepositoryFactory.SQL_DB);
        assert repositoryFactory != null;
        estudianteRepository = (EstudianteRepository) repositoryFactory.getEstudianteRepository();
        carreraRepository = (CarreraRepository) repositoryFactory.getCarreraRepository();
        carreraEstudianteRepository = (CarreraEstudianteRepository) repositoryFactory.getCarreraEstudianteRepository();

        llenarTablaEstudiante(datosEstudiantes);
        llenarTablaCarrera(datosCarreras);
        llenarTablaCarreraEstudiante(datosCarrerasEstudiantes);

        //Ejercicio 2)a
        Estudiante est = new Estudiante("Diego","Lopez",23,"Male",42177291,"Tandil",249264);
        estudianteRepository.guardarEstudiante(est);
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 2)a");
        System.out.println(estudianteRepository.obtenerEstudiantePorDni(42708154));

        //Ejercicio 2)b
        Carrera c = carreraRepository.getCarreraId(1);
        Carrera_EstudiantePK pk = new Carrera_EstudiantePK(est, c);
        Timestamp fechaInscripcion = Timestamp.valueOf(LocalDateTime.now());
        Carrera_Estudiante matricula = new Carrera_Estudiante(pk, fechaInscripcion,null,1);
        carreraEstudianteRepository.matricularEstudianteEnCarrera(matricula);

        //Ejercicio 2)c
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 2)c");
        List<Estudiante> estudiantesOrdenados = estudianteRepository.obtenerTodosLosEstudiantesOrdenadosApellido();
        for (Estudiante e : estudiantesOrdenados){
            System.out.println(e);
        }

        //Ejercicio 2)d
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 2)d");
        System.out.println(estudianteRepository.obtenerEstudiantePorLibretaUniversitaria(75247));

        //Ejercicio 2)e
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 2)e");
        List<Estudiante> estudiantesGeneros = estudianteRepository.obtenerEstudiantesPorGenero("Female");
        for (Estudiante estudiante : estudiantesGeneros){
            System.out.println(estudiante);
        }

        //Ejercicio 2)f
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 2)f");
        List<CarreraInscriptosDTO> carrerasOrdenadasCantidadInscriptos = carreraEstudianteRepository.obtenerCarrerasConInscriptos();
        for (CarreraInscriptosDTO carre : carrerasOrdenadasCantidadInscriptos){
            System.out.println(carre);
        }

        //Ejercicio 2)g
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 2)g");
        List<EstudiantesPorCarreraPorCiudadDTO> estudiantesCarreraCiudad = carreraEstudianteRepository.obtenerEstudiantesDeCarreraPorCiudad("TUDAI","Tandil");
        for (EstudiantesPorCarreraPorCiudadDTO est1 : estudiantesCarreraCiudad){
            System.out.println(est1);
        }

        //Ejercicio 3)
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ejercicio 3)");
        List<ReporteCarrerasDTO> reporteCarreras = carreraEstudianteRepository.getInscriptosYEgresadosPorAnio();
        for (ReporteCarrerasDTO reporte : reporteCarreras){
            System.out.println(reporte);
        }
    }


    public static void llenarTablaCarreraEstudiante(CSVParser parser) {
        for (CSVRecord row : parser) {
            int idEstudiante = Integer.parseInt(row.get("id_estudiante"));
            int idCarrera = Integer.parseInt(row.get("id_carrera"));
            String inscripcion = row.get("inscripcion");
            String graduacion = row.get("graduacion");
            int antiguedad = Integer.parseInt(row.get("antiguedad"));
            //se busca el estudiante y la carrera
            Estudiante e = estudianteRepository.obtenerEstudiantePorDni(idEstudiante);
            Carrera c = carreraRepository.getCarreraId(idCarrera);
            Carrera_EstudiantePK pk = new Carrera_EstudiantePK(e,c);

            String fechaInscripcion = inscripcion+"-01-01 00:00:00";
            Carrera_Estudiante ec;
            if(graduacion.equals("0000")){ //pregunta si no se graduo
                ec = new Carrera_Estudiante(pk, Timestamp.valueOf(fechaInscripcion),null,antiguedad);
            }
            else {
                String fechagraduacion = graduacion+"-01-01 00:00:00";
                ec = new Carrera_Estudiante(pk,Timestamp.valueOf(fechaInscripcion),Timestamp.valueOf(fechagraduacion),antiguedad);
            }
            carreraEstudianteRepository.matricularEstudianteEnCarrera(ec);
        }
    }

    public static void llenarTablaEstudiante(CSVParser parser){
        for (CSVRecord row : parser){
            int dni = Integer.parseInt(row.get("DNI"));
            String nombre = row.get("nombre");
            String apellido = row.get("apellido");
            int edad = Integer.parseInt(row.get("edad"));
            String genero = row.get("genero");
            String ciudad = row.get("ciudad");
            int nroLibreta = Integer.parseInt(row.get("LU"));
            Estudiante est = new Estudiante(nombre, apellido, edad, genero, dni, ciudad, nroLibreta);
            estudianteRepository.guardarEstudiante(est);
        }
    }

    public static void llenarTablaCarrera(CSVParser parser) {
        for (CSVRecord row : parser){
            String nombre = row.get("carrera");
            int duracion = Integer.parseInt(row.get("duracion"));
            Carrera carrera = new Carrera(nombre, duracion);
            carreraRepository.guardarCarrera(carrera);
        }
    }
}