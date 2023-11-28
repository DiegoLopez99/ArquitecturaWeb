package com.tpe.microservicioadministracion.repository;

import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
}
