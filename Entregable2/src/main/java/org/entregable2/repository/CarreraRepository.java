package org.entregable2.repository;

import org.entregable2.entity.Carrera;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CarreraRepository implements InterfazCarreraRepository{
    private final EntityManagerFactory emf;

    public CarreraRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Carrera guardarCarrera(Carrera carrera) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (!em.contains(carrera)){
            em.persist(carrera);
        }else {
            em.merge(carrera);
        }
        em.getTransaction().commit();
        em.close();
        return carrera;
    }


    @Override
    public Carrera obtenerCarreraPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Carrera carrera = em.find(Carrera.class, id);
        em.close();
        return carrera;
    }

    @Override
    public List<Carrera> obtenerTodasLasCarrerasOrdenadas() {
        EntityManager em = emf.createEntityManager();
        List<Carrera> carreras = em.createQuery("SELECT c FROM Carrera c ORDER BY c.id", Carrera.class)
                .getResultList();
        em.close();
        return carreras;
    }

    @Override
    public Carrera getCarreraId(long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Carrera c = em.find(Carrera.class, id);
        em.getTransaction().commit();
        em.close();
        return c;
    }
}
