package org.example.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.Cliente;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlClienteDao implements ClienteDao{
    private final Connection conn;

    public MySqlClienteDao(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        conn.setAutoCommit(false);
        String table = "CREATE TABLE cliente (" +
                "id int  NOT NULL," +
                "nombre varchar(200)  NOT NULL," +
                "email varchar(200)  NOT NULL," +
                "CONSTRAINT Cliente_pk PRIMARY KEY (id)" +
                ");";
        conn.prepareStatement(table).execute();
        conn.commit();
    }

    private void addCliente(Cliente cliente) throws SQLException {
        conn.setAutoCommit(false);
        String insert = "INSERT INTO cliente (id, nombre, email) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setInt(1,cliente.getId());
        ps.setString(2, cliente.getNombre());
        ps.setString(3, cliente.getEmail());
        ps.executeUpdate();
        ps.close();
        conn.commit();
    }

    public void readCSV(String path){
        CSVParser parser;
        try {
            parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(path));
            for (CSVRecord row : parser){
                int id = Integer.parseInt(row.get("idCliente"));
                String nombre = row.get("nombre");
                String email = row.get("email");
                Cliente cliente = new Cliente(id, nombre, email);
                this.addCliente(cliente);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Cliente> getClientesMasFacturaron(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            conn.setAutoCommit(false);
            String select = "SELECT " +
                                "c.id AS idCliente, " +
                                "c.nombre AS nombreCliente, " +
                                "c.email AS emailCliente, " +
                                "SUM(fp.cantidad * p.valor) AS totalFacturado " +
                            "FROM " +
                                "cliente c " +
                            "JOIN " +
                                "factura f ON c.id = f.idCliente " +
                            "JOIN " +
                                "factura_producto fp ON f.id = fp.idFactura " +
                            "JOIN " +
                                "producto p ON fp.idProducto = p.id " +
                            "GROUP BY " +
                                "c.id, c.nombre, c.email " +
                            "ORDER BY " +
                                "totalFacturado DESC;";
            PreparedStatement ps = conn.prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                String email = rs.getString(3);
                clientes.add(new Cliente(id, nombre, email));
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return clientes;
    }
}
