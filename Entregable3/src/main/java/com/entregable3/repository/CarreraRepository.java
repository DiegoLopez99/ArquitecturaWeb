package com.entregable3.repository;

import com.entregable3.domain.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findAllByOrderByNombreAsc();
    Optional<Carrera> findById(Long id);
}
