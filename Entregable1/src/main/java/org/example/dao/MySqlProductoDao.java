package org.example.dao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.Producto;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlProductoDao implements ProductoDao{
    private final Connection conn;

    public MySqlProductoDao(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        conn.setAutoCommit(false);
        String table = "CREATE TABLE producto (" +
                "id int  NOT NULL," +
                "nombre varchar(200)  NOT NULL," +
                "valor float(15,7)  NOT NULL," +
                "CONSTRAINT Producto_pk PRIMARY KEY (id)" +
                ")";
        conn.prepareStatement(table).execute();
        conn.commit();
    }

    private void addProducto(Producto prod) throws SQLException {
        conn.setAutoCommit(false);
        String insert = "INSERT INTO producto (id, nombre, valor) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setInt(1,prod.getId());
        ps.setString(2, prod.getNombre());
        ps.setFloat(3, prod.getValor());
        ps.executeUpdate();
        ps.close();
        conn.commit();
    }

    public void readCSV(String path){
        CSVParser parser;
        try {
            parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(path));
            for (CSVRecord row : parser){
                int id = Integer.parseInt(row.get("idProducto"));
                String nombre = row.get("nombre");
                float valor = Float.parseFloat(row.get("valor"));
                Producto prod = new Producto(id, nombre, valor);
                this.addProducto(prod);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Producto obtenerProductoConMasRecaudacion() {
        Producto result = new Producto();
        try {
            conn.setAutoCommit(false);
            String select = "SELECT p.id AS idProducto, p.nombre AS nombreProducto, p.valor AS valorProducto, " +
                    "SUM(fp.cantidad * p.valor) AS recaudacion " +
                    "FROM producto p " +
                    "JOIN factura_producto fp ON p.id = fp.idProducto " +
                    "GROUP BY p.id, p.nombre, p.valor " +
                    "ORDER BY recaudacion DESC " +
                    "LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(select);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                float valor = rs.getFloat(3);
                result.setId(id);
                result.setNombre(nombre);
                result.setValor(valor);
                return result;
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
