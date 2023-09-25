package org.entregable2.repository;

import org.entregable2.dto.CarreraInscriptosDTO;
import org.entregable2.dto.EstudiantesPorCarreraPorCiudadDTO;
import org.entregable2.dto.ReporteCarrerasDTO;
import org.entregable2.entity.Carrera_Estudiante;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class CarreraEstudianteRepository implements InterfazCarreraEstudianteRepository{
    private final EntityManagerFactory emf;

    public CarreraEstudianteRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Carrera_Estudiante matricularEstudianteEnCarrera(Carrera_Estudiante carreraEstudiante) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Carrera_Estudiante existeCarreraEstudiante = em.find(Carrera_Estudiante.class, carreraEstudiante.getPrimaryKeys());
        if (existeCarreraEstudiante == null) {
            em.persist(carreraEstudiante);
        } else {
            em.merge(carreraEstudiante);
        }
        em.getTransaction().commit();
        em.close();
        return carreraEstudiante;
    }
    @Override
    public List<CarreraInscriptosDTO> obtenerCarrerasConInscriptos() {
        EntityManager em = emf.createEntityManager();
        List<CarreraInscriptosDTO> resultados = em.createQuery("SELECT NEW org.entregable2.dto.CarreraInscriptosDTO(c, COUNT(ce.primaryKeys.estudiante.dni)) "+
                        "FROM Carrera_Estudiante ce " +
                        "JOIN ce.primaryKeys.carrera c "+
                        "GROUP BY c "+
                        "ORDER BY COUNT(ce.primaryKeys.estudiante.dni) DESC", CarreraInscriptosDTO.class)
                .getResultList();
        em.close();
        return resultados;
    }

    @Override
    public List<EstudiantesPorCarreraPorCiudadDTO> obtenerEstudiantesDeCarreraPorCiudad(String carrera, String ciudad){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<EstudiantesPorCarreraPorCiudadDTO> estudiantes = em.createQuery(
                                "SELECT NEW org.entregable2.dto.EstudiantesPorCarreraPorCiudadDTO(ce.primaryKeys.estudiante.dni, e.nombre, e.apellido, c.nombre, e.ciudad) "+
                                    "FROM Carrera_Estudiante ce JOIN ce.primaryKeys.estudiante e " +
                                "JOIN ce.primaryKeys.carrera c "+
                                "WHERE e.ciudad = :ciudad " +
                                "AND c.nombre LIKE :carrera "+
                                "GROUP BY ce.primaryKeys.estudiante.dni, e.nombre, e.apellido, c.nombre, e.ciudad")
                .setParameter("ciudad", ciudad)
                .setParameter("carrera", carrera)
                .getResultList();
        em.getTransaction().commit();
        em.close();
        return estudiantes;
    }

    @Override
    public List<ReporteCarrerasDTO> getInscriptosYEgresadosPorAnio() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String jpql = "SELECT c.nombre AS nombreCarrera, " +
                "EXTRACT(YEAR FROM ce.fechaInscripcion) AS anio, " +
                "COUNT(ce) AS cantInscriptos, " +
                "SUM(CASE WHEN ce.graduacion IS NOT NULL THEN 1 ELSE 0 END) AS cantEgresados " +
                "FROM Carrera_Estudiante ce " +
                "JOIN ce.primaryKeys.carrera c " +
                "GROUP BY c.nombre, anio " +
                "ORDER BY c.nombre ASC, anio ASC";

        Query query = em.createQuery(jpql);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(result -> new ReporteCarrerasDTO(
                        (String) result[0],
                        (Integer) result[1], // Año
                        (Long) result[2],  // Cantidad de inscriptos
                        (Long) result[3]   // Cantidad de egresados
                ))
                .collect(Collectors.toList());
    }

}
