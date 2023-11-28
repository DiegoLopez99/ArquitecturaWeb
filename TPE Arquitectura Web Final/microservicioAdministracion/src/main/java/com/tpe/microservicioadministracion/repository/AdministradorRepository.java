package com.tpe.microservicioadministracion.repository;

import com.tpe.microservicioadministracion.domain.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
}
