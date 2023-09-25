package org.entregable2.repository;

import org.entregable2.entity.Carrera;
import java.util.List;

public interface InterfazCarreraRepository {
    Carrera guardarCarrera(Carrera carrera);
    Carrera obtenerCarreraPorId(Long id);
    List<Carrera> obtenerTodasLasCarrerasOrdenadas();
     Carrera getCarreraId(long id);
}
