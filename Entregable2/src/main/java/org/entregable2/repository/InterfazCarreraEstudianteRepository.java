package org.entregable2.repository;

import org.entregable2.dto.CarreraInscriptosDTO;
import org.entregable2.dto.EstudiantesPorCarreraPorCiudadDTO;
import org.entregable2.dto.ReporteCarrerasDTO;
import org.entregable2.entity.Carrera_Estudiante;

import java.util.List;

public interface InterfazCarreraEstudianteRepository {
    Carrera_Estudiante matricularEstudianteEnCarrera(Carrera_Estudiante carreraEstudiante);
    List<CarreraInscriptosDTO> obtenerCarrerasConInscriptos();
    List<EstudiantesPorCarreraPorCiudadDTO> obtenerEstudiantesDeCarreraPorCiudad(String carrera, String ciudad);
    List<ReporteCarrerasDTO> getInscriptosYEgresadosPorAnio();
}
