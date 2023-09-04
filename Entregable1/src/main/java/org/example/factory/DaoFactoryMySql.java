package org.example.factory;
import org.example.dao.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactoryMySql extends DaoFactory{
    static DaoFactoryMySql mySqlFactory;

    private  DaoFactoryMySql(){

    }

    public static DaoFactoryMySql getMySqlFactory() {
        if(conn == null){
            mySqlFactory = new DaoFactoryMySql();
        }
        return mySqlFactory;
    }

    @Override
    public ClienteDao MySqlClienteDao(Connection conn) {
        return new MySqlClienteDao(conn);
    }

    @Override
    public FacturaDao MySqlFacturaDao(Connection conn) {
        return new MySqlFacturaDao(conn);
    }

    @Override
    public ProductoDao MySqlProductoDao(Connection conn) {
        return new MySqlProductoDao(conn);
    }

    @Override
    public FacturaProductoDao MySqlFacturaProductoDao(Connection conn) {
        return new MySqlFacturaProductoDao(conn);
    }

    @Override
    public Connection getConnection() throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        }catch (InstantiationException | IllegalAccessException | IllegalArgumentException |
                NoSuchMethodException | SecurityException | ClassNotFoundException | InvocationTargetException e){
            e.printStackTrace();
            System.exit(1);
        }
        //String URI = "jdbc:mysql://localhost:3306/ejemplo_mysql_docker";
        String URI = "jdbc:mysql://localhost:3306/entregable1";
        //return DriverManager.getConnection(URI, "root", "diegolopez");
        return DriverManager.getConnection(URI, "root", "");
    }
}
