package microservice.Repository;

import microservice.Model.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MonopatinRepository extends JpaRepository<Monopatin,Long> {
    @Query("SELECT m FROM Monopatin m WHERE m.km_recorridos BETWEEN :kmMinimos AND :kmMaximos")
    List<Monopatin> obtenerMonopatinesPorKM(@Param("kmMinimos") float kmMinimos, @Param("kmMaximos") float kmMaximos);
    @Query("SELECT m from Monopatin m where m.ubicacion = :ubi")
    List<Monopatin> obtenerMonmopatinesPorUbicacion(@Param("ubi") String ubi);
    @Query("SELECT count(m) from Monopatin m where m.estado = 'mantenimiento' ")
    int obtenerCantidadEnMantenimiento();

    @Query("SELECT count(m) from Monopatin m where m.estado != 'mantenimiento' ")
    int obtenerCantidadEnServicio();
}
