package com.tpe.viaje.repository;

import com.tpe.viaje.DTO.MonopatinPorUso;
import com.tpe.viaje.DTO.TotalFacturadoDTO;
import com.tpe.viaje.Entity.Viaje;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ViajeRepositoryCustom {
    TotalFacturadoDTO calcularTotalFacturadoEnRango(Integer mesInicio, Integer mesFinal, Integer anio);

    List<Long> obtenerIdMonopatinesMasViajesAnio(int x, int anio);

    List<MonopatinPorUso> obtenerUsoMonopatin();
}
