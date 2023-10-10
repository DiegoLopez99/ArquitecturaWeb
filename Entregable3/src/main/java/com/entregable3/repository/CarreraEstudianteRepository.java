package com.entregable3.repository;

import com.entregable3.domain.Carrera_Estudiante;
import com.entregable3.domain.Carrera_EstudiantePK;
import com.entregable3.dto.CarreraInscriptosDTO;
import com.entregable3.dto.EstudiantesPorCarreraPorCiudadDTO;
import com.entregable3.dto.ReporteCarrerasDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarreraEstudianteRepository extends JpaRepository<Carrera_Estudiante, Carrera_EstudiantePK> {
    Carrera_Estudiante save(Carrera_Estudiante matricula);

    @Query("SELECT NEW com.entregable3.dto.CarreraInscriptosDTO(c, COUNT(ce.primaryKeys.estudiante.dni)) " +
            "FROM Carrera_Estudiante ce JOIN ce.primaryKeys.carrera c " +
            "GROUP BY c " +
            "ORDER BY COUNT(ce.primaryKeys.estudiante.dni) DESC")
    List<CarreraInscriptosDTO> obtenerCarrerasConInscriptos();

    @Query("SELECT NEW com.entregable3.dto.EstudiantesPorCarreraPorCiudadDTO(ce.primaryKeys.estudiante.dni, e.nombre, e.apellido, c.nombre, e.ciudad) " +
            "FROM Carrera_Estudiante ce JOIN ce.primaryKeys.estudiante e JOIN ce.primaryKeys.carrera c " +
            "WHERE e.ciudad = :ciudad AND c.nombre LIKE :carrera " +
            "GROUP BY ce.primaryKeys.estudiante.dni, e.nombre, e.apellido, c.nombre, e.ciudad")
    List<EstudiantesPorCarreraPorCiudadDTO> obtenerEstudiantesDeCarreraPorCiudad(@Param("ciudad") String ciudad, @Param("carrera") String carrera);

    @Query("SELECT NEW com.entregable3.dto.ReporteCarrerasDTO(c.nombre, " +
            "FUNCTION('YEAR', ce.fechaInscripcion), " +
            "COUNT(ce), " +
            "SUM(CASE WHEN ce.graduacion IS NOT NULL THEN 1 ELSE 0 END)) " +
            "FROM Carrera_Estudiante ce " +
            "JOIN ce.primaryKeys.carrera c " +
            "GROUP BY c.nombre, FUNCTION('YEAR', ce.fechaInscripcion) " +
            "ORDER BY c.nombre ASC, FUNCTION('YEAR', ce.fechaInscripcion) ASC")
    List<ReporteCarrerasDTO> getInscriptosYEgresadosPorAnio();
}
