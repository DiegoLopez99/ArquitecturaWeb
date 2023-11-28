package com.tpe.viaje.repository;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PausaRepositoryCustom {

    List<Pausa> findAllByViaje(Viaje viaje);

    List<Pausa> findByMonopatinId(@PathVariable("id") Long id);

    List<List<Integer>> findUsoByMonopatinId();

}
