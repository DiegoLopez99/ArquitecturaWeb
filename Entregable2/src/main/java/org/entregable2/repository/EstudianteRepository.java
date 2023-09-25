package org.entregable2.repository;

import org.entregable2.entity.Estudiante;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class EstudianteRepository implements InterfazEstudianteRepository{
    private final EntityManagerFactory emf;

    public EstudianteRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(estudiante);
        em.getTransaction().commit();
        em.close();
        return estudiante;
    }

    @Override
    public Estudiante obtenerEstudiantePorDni(int dni) {
        EntityManager em = emf.createEntityManager();
        Estudiante estudiante = em.find(Estudiante.class, dni);
        em.close();
        return estudiante;
    }

    @Override
    public List<Estudiante> obtenerTodosLosEstudiantesOrdenadosApellido() {
        EntityManager em = emf.createEntityManager();
        List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e ORDER BY e.apellido", Estudiante.class)
                .getResultList();
        em.close();
        return estudiantes;
    }

    @Override
    public Estudiante obtenerEstudiantePorLibretaUniversitaria(int numLibreta) {
        EntityManager em = emf.createEntityManager();
        Estudiante estudiante = em.createQuery("SELECT e FROM Estudiante e " +
                                                   "WHERE e.nroLibreta = :libreta", Estudiante.class)
                .setParameter("libreta", numLibreta)
                .getSingleResult();
        em.close();
        return estudiante;
    }

    @Override
    public List<Estudiante> obtenerEstudiantesPorGenero(String genero) {
        EntityManager em = emf.createEntityManager();
        List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e " +
                                                          "WHERE e.genero = :genero", Estudiante.class)
                .setParameter("genero", genero)
                .getResultList();
        em.close();
        return estudiantes;
    }

    @Override
    public List<Estudiante> obtenerEstudiantesDeCarreraPorCiudad(Long carreraId, String ciudad) {
        EntityManager em = emf.createEntityManager();
        List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e " +
                                                          "JOIN e.carreras ec " +
                                                          "WHERE ec.primaryKeys.id_carrera = :carreraId " +
                                                           "AND e.ciudadResidencia = :ciudad", Estudiante.class)
                .setParameter("carreraId", carreraId)
                .setParameter("ciudad", ciudad)
                .getResultList();
        em.close();
        return estudiantes;
    }

}
