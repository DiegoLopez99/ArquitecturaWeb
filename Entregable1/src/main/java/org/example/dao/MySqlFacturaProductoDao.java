package org.example.dao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.FacturaProducto;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlFacturaProductoDao implements FacturaProductoDao{
    private final Connection conn;

    public MySqlFacturaProductoDao(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        conn.setAutoCommit(false);
        String table = "CREATE TABLE factura_producto (" +
                "idFactura int  NOT NULL," +
                "idProducto int  NOT NULL," +
                "cantidad int  NOT NULL," +
                "CONSTRAINT factura_producto_pk PRIMARY KEY (idFactura, idProducto)" +
                ");";
        String addConstraint1 = "ALTER TABLE factura_producto " +
                        "ADD CONSTRAINT Factura_Producto_Factura " +
                        "FOREIGN KEY (idFactura) " +
                        "REFERENCES factura (id)" +
                        ";";
        String addConstraint2 ="ALTER TABLE factura_producto " +
                "ADD CONSTRAINT Factura_Producto_Producto " +
                "FOREIGN KEY (idProducto) " +
                "REFERENCES producto (id)" +
                ";";
        conn.prepareStatement(table).execute();
        conn.prepareStatement(addConstraint1).execute();
        conn.prepareStatement(addConstraint2).execute();
        conn.commit();
    }

    private void addFacturaProducto(FacturaProducto facturaProducto) throws SQLException {
        conn.setAutoCommit(false);
        String insert = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setInt(1,facturaProducto.getIdFactura());
        ps.setInt(2, facturaProducto.getIdProducto());
        ps.setInt(3, facturaProducto.getCantidad());
        ps.executeUpdate();
        ps.close();
        conn.commit();
    }

    public void readCSV(String path){
        CSVParser parser;
        try {
            parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(path));
            for (CSVRecord row : parser){
                int idFactura = Integer.parseInt(row.get("idFactura"));
                int idProducto = Integer.parseInt(row.get("idProducto"));
                int cantidad = Integer.parseInt(row.get("cantidad"));
                FacturaProducto facturaProducto = new FacturaProducto(idFactura, idProducto, cantidad);
                this.addFacturaProducto(facturaProducto);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
