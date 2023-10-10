package com.entregable3.repository;

import com.entregable3.domain.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    List<Estudiante> findAllByOrderByApellidoAsc();

    Optional<Estudiante> findByDni(int dni);

    Optional<Estudiante> findByNroLibreta(int numLibreta);

    List<Estudiante> findByGenero(String genero);

}
