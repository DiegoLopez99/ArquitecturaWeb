package org.entregable2.factory;

import org.entregable2.repository.InterfazCarreraEstudianteRepository;
import org.entregable2.repository.InterfazCarreraRepository;
import org.entregable2.repository.InterfazEstudianteRepository;

import java.sql.SQLException;

public abstract class RepositoryFactory {
    public static final int SQL_DB = 1;
    public abstract InterfazEstudianteRepository getEstudianteRepository();
    public abstract InterfazCarreraRepository getCarreraRepository();
    public abstract InterfazCarreraEstudianteRepository getCarreraEstudianteRepository();

    public abstract void closeEMF();

    public static RepositoryFactory getEntityManagerFactory(int persis) throws SQLException {
        switch (persis) {
            case SQL_DB : return SQLFactory.getInstance();
            default: return null;
        }
    }
}
