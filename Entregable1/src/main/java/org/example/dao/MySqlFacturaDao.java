package org.example.dao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.Factura;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlFacturaDao implements FacturaDao{
    private final Connection conn;

    public MySqlFacturaDao(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        conn.setAutoCommit(false);
        String table = "CREATE TABLE factura (" +
                "id int  NOT NULL," +
                "idCliente int  NOT NULL," +
                "CONSTRAINT Factura_pk PRIMARY KEY (id)" +
                ");";
        String addConstraint = "ALTER TABLE factura " +
                "ADD CONSTRAINT factura_cliente " +
                "FOREIGN KEY factura_cliente (idCliente)" +
                "REFERENCES cliente (id)" +
                ";";
        conn.prepareStatement(table).execute();
        conn.prepareStatement(addConstraint).execute();
        conn.commit();
    }

    private void addFactura(Factura factura) throws SQLException {
        conn.setAutoCommit(false);
        String insert = "INSERT INTO factura (id, idCliente) VALUES (?,?)";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setInt(1,factura.getId());
        ps.setInt(2, factura.getIdCliente());
        ps.executeUpdate();
        ps.close();
        conn.commit();
    }

    public void readCSV(String path){
        CSVParser parser;
        try {
            parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(path));
            for (CSVRecord row : parser){
                int id = Integer.parseInt(row.get("idFactura"));
                int idCliente = Integer.parseInt(row.get("idCliente"));
                Factura factura = new Factura(id, idCliente);
                this.addFactura(factura);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
