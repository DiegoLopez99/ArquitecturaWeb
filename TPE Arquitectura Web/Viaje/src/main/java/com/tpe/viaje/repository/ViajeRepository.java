package com.tpe.viaje.repository;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
@Repository
public interface ViajeRepository extends JpaRepository<Viaje,Long> {

    List<Viaje> findAllByOrderByKmAsc();
   @Query("select v.id_monopatin from Viaje  v where extract(year from v.inicio)= :anio group by v.id_monopatin having count(v) < :x ")
    List<Long> findByid_monopatin(@Param("x")int x, @Param("anio") int anio );

    @Query("select sum(extract(hour from v.fin)-extract(hour from v.inicio))  as hora,sum(extract(minute from v.fin)-extract(minute from v.inicio)) as minuto, v.id_monopatin " +
            "from Viaje v  group by v.id_monopatin ")
    List<List<Integer>> findByid_Monopatin();

    @Query("SELECT COALESCE(SUM(v.costo), 0) FROM Viaje v " +
            "WHERE MONTH(v.inicio) >= :mesInicio " +
            "AND MONTH(v.inicio) <= :mesFinal " +
            "AND YEAR(v.inicio) = :anio")
    float calcularTotalFacturadoEnRango(@Param("mesInicio") Integer mesInicio,
                                        @Param("mesFinal") Integer mesFinal,
                                        @Param("anio") Integer anio);
}