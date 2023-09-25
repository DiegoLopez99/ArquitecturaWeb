package org.entregable2.factory;

import org.entregable2.repository.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SQLFactory extends RepositoryFactory{
    private static SQLFactory instance;
    private final EntityManagerFactory emf;

    private SQLFactory() {
        emf = Persistence.createEntityManagerFactory("entregable2db");
    }

    public static SQLFactory getInstance() {
        if (instance == null) {
            instance = new SQLFactory();
        }
        return instance;
    }

    @Override
    public InterfazEstudianteRepository getEstudianteRepository() {
        return new EstudianteRepository(emf);
    }

    @Override
    public InterfazCarreraRepository getCarreraRepository() {
        return new CarreraRepository(emf);
    }

    @Override
    public InterfazCarreraEstudianteRepository getCarreraEstudianteRepository() {
        return new CarreraEstudianteRepository(emf);
    }

    @Override
    public void closeEMF() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
