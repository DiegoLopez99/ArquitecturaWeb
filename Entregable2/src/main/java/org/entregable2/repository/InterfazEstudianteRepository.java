package org.entregable2.repository;

import org.entregable2.entity.Estudiante;

import java.util.List;

public interface InterfazEstudianteRepository {
    Estudiante guardarEstudiante(Estudiante estudiante);
    Estudiante obtenerEstudiantePorDni(int DNI);
    List<Estudiante> obtenerTodosLosEstudiantesOrdenadosApellido();
    Estudiante obtenerEstudiantePorLibretaUniversitaria(int numLibreta);
    List<Estudiante> obtenerEstudiantesPorGenero(String genero);
    List<Estudiante> obtenerEstudiantesDeCarreraPorCiudad(Long carreraId, String ciudad);
}
