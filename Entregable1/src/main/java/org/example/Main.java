package org.example;
import org.example.dao.*;
import org.example.factory.DaoFactory;
import org.example.model.Cliente;
import org.example.model.Producto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {

        //Instancio la conexion MySql
        DaoFactory factory = DaoFactory.getDaoFactory(1);
        assert factory != null;
        Connection conn = factory.getConnection();

        //Instancio la clase DAO MySql de cada entidad
        MySqlClienteDao clienteDao = (MySqlClienteDao) factory.MySqlClienteDao(conn);
        MySqlFacturaDao facturaDao = (MySqlFacturaDao) factory.MySqlFacturaDao(conn);
        MySqlFacturaProductoDao facturaProductoDao = (MySqlFacturaProductoDao) factory.MySqlFacturaProductoDao(conn);
        MySqlProductoDao productoDao = (MySqlProductoDao) factory.MySqlProductoDao(conn);


        //Guardo las direcciones de los archivos csv
        String pathCliente = "C:/Users/lopez/Desktop/csv/clientes.csv";
        String pathFactura = "C:/Users/lopez/Desktop/csv/facturas.csv";
        String pathFacturasProductos = "C:/Users/lopez/Desktop/csv/facturas-productos.csv";
        String pathProductos = "C:/Users/lopez/Desktop/csv/productos.csv";

        //Creo las tablas en la base
        clienteDao.createTable();
        facturaDao.createTable();
        productoDao.createTable();
        facturaProductoDao.createTable();

        //Inserto los datos en las tablas
        clienteDao.readCSV(pathCliente);
        facturaDao.readCSV(pathFactura);
        productoDao.readCSV(pathProductos);
        facturaProductoDao.readCSV(pathFacturasProductos);

        //Imprimo servicio del ejercicio 3: Producto que mas recaudo
        Producto producto = productoDao.obtenerProductoConMasRecaudacion();
        System.out.println("Producto que mas reacaudo:");
        System.out.println("Id: "+producto.getId()+", Nombre: "+producto.getNombre()+", Valor: "+producto.getValor());

        //imprimo servicio del ejercicio 4:  lista de clientes ordenada por a cuál se le
        //facturó más.
        ArrayList<Cliente> clientes = clienteDao.getClientesMasFacturaron();
        System.out.println("Lista de clientes que mas facturaron a menos:");
        for (Cliente c : clientes){
            System.out.println("ID: "+c.getId()+", "+"Nombre: "+c.getNombre()+", "+"Email: "+c.getEmail()+";");
        }
    }
}
