package org.example.dao;

import org.example.model.Producto;

import java.sql.SQLException;

public interface ProductoDao {
    void createTable() throws SQLException;
    void readCSV(String path);
    Producto obtenerProductoConMasRecaudacion();
}
