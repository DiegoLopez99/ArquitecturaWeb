package com.entregable3.service;

import com.entregable3.domain.Carrera;
import com.entregable3.domain.Carrera_Estudiante;
import com.entregable3.domain.Carrera_EstudiantePK;
import com.entregable3.domain.Estudiante;
import com.entregable3.dto.CarreraInscriptosDTO;
import com.entregable3.dto.EstudiantesPorCarreraPorCiudadDTO;
import com.entregable3.dto.ReporteCarrerasDTO;
import com.entregable3.repository.CarreraEstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CarreraEstudianteService {
    private final CarreraEstudianteRepository carreraEstudianteRepository;

    @Autowired
    public CarreraEstudianteService(CarreraEstudianteRepository carreraEstudianteRepository) {
        this.carreraEstudianteRepository = carreraEstudianteRepository;
    }

    public Carrera_Estudiante matricularEstudianteEnCarrera(Estudiante estudiante, Carrera carrera, String anioInscripcion, String anioGraduacion, int antiguedad) {
        Carrera_EstudiantePK primaryKey = new Carrera_EstudiantePK(estudiante, carrera);
        String fechaInscripcion = anioInscripcion+"-01-01 00:00:00";
        Carrera_Estudiante matricula;
        if(anioGraduacion.equals("0000")){ //pregunta si no se graduo
            matricula = new Carrera_Estudiante(primaryKey, Timestamp.valueOf(fechaInscripcion),null,antiguedad);
        }
        else {
            String fechagraduacion = anioGraduacion+"-01-01 00:00:00";
            matricula = new Carrera_Estudiante(primaryKey,Timestamp.valueOf(fechaInscripcion),Timestamp.valueOf(fechagraduacion),antiguedad);
        }
        return carreraEstudianteRepository.save(matricula);
    }

    public List<EstudiantesPorCarreraPorCiudadDTO> obtenerEstudiantesDeCarreraPorCiudad(String ciudad, String carrera) {
        return carreraEstudianteRepository.obtenerEstudiantesDeCarreraPorCiudad(ciudad, carrera);
    }

    public List<CarreraInscriptosDTO> obtenerCarrerasConInscriptos() {
        return carreraEstudianteRepository.obtenerCarrerasConInscriptos();
    }

    public List<ReporteCarrerasDTO> getInscriptosYEgresadosPorAnio() {
        return carreraEstudianteRepository.getInscriptosYEgresadosPorAnio();
    }

}
