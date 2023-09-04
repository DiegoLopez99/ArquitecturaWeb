package org.example.dao;

import org.example.model.Cliente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ClienteDao {
    void createTable() throws SQLException;
    void readCSV(String path);
    ArrayList<Cliente> getClientesMasFacturaron();
}
