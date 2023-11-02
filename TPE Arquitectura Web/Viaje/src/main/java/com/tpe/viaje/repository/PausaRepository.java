package com.tpe.viaje.repository;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface PausaRepository extends JpaRepository<Pausa,Long> {

    List<Pausa> findAllByViaje(Viaje viaje);

    @Query("select p from Pausa p where p.viaje.id_monopatin = :id")
    List<Pausa> findByMonopatinId(@PathVariable("id") Long id);
    @Query("select sum(extract(hour from p.tiempo )) as hora, sum(extract(minute from p.tiempo )) as minutos, p.viaje.id_monopatin " +
            "from Pausa p  group by p.viaje.id_monopatin")
    List<List<Integer>> findUsoByMonopatinId();

}
