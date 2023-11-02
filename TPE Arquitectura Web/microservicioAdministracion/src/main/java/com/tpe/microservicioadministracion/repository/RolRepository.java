package com.tpe.microservicioadministracion.repository;

import com.tpe.microservicioadministracion.domain.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
