package org.example.factory;
import org.example.dao.ClienteDao;
import org.example.dao.FacturaDao;
import org.example.dao.FacturaProductoDao;
import org.example.dao.ProductoDao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DaoFactory {
    public static final int MYSQL_JDBC = 1;
    public static final int DERBY_JDBC = 2;
    public static final int JPA_HIBERNATE = 3;
    public static DaoFactory conn;



    public abstract ClienteDao MySqlClienteDao(Connection conn) throws SQLException;
    public abstract FacturaDao MySqlFacturaDao(Connection conn) throws SQLException;
    public abstract ProductoDao MySqlProductoDao(Connection conn) throws SQLException;
    public abstract FacturaProductoDao MySqlFacturaProductoDao(Connection conn) throws SQLException;
    public abstract Connection getConnection() throws SQLException;


    public static DaoFactory getDaoFactory(int f) throws SQLException {
        if(conn == null){
            switch (f){
                case MYSQL_JDBC -> {
                    conn =  DaoFactoryMySql.getMySqlFactory();
                    return conn;
                }
                case DERBY_JDBC -> {
                    // Conexion con Derby
                    return null;
                }
                case JPA_HIBERNATE -> {
                    // Conexion con JPA
                    return null;
                }
            }
        }

        return conn;
    }
}
