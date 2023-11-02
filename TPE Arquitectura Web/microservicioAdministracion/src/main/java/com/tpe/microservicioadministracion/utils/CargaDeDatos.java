package com.tpe.microservicioadministracion.utils;

import com.tpe.microservicioadministracion.domain.entity.Administrador;
import com.tpe.microservicioadministracion.domain.entity.Rol;
import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import com.tpe.microservicioadministracion.service.AdministradorService;
import com.tpe.microservicioadministracion.service.RolService;
import com.tpe.microservicioadministracion.service.TarifaService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;

@Component
public class CargaDeDatos {
    @Autowired
    private final AdministradorService administradorService;
    @Autowired
    private final TarifaService tarifaService;
    @Autowired
    private final RolService rolService;

    public CargaDeDatos(AdministradorService administradorService, TarifaService tarifaService, RolService rolService) {
        this.administradorService = administradorService;
        this.tarifaService = tarifaService;
        this.rolService = rolService;
    }

    public void llenarTablaAministradores() throws IOException {
        CSVParser datosAdministradores = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/administradores.csv"));
        for (CSVRecord row : datosAdministradores){
            String nombreUsuario = row.get("nombre_usuario");
            String contrasenia = row.get("contrasenia");
            Long rol_id = Long.parseLong(row.get("rol_id"));
            Rol rol = rolService.getRolId(rol_id);
            Administrador admin = new Administrador(nombreUsuario, contrasenia, rol);
            administradorService.guardarAdministrador(admin);
        }
    }

    public void llenarTablaTarifas() throws IOException {
        CSVParser datosTarifas = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/tarifas.csv"));
        for (CSVRecord row : datosTarifas){
            String fechaInicio = row.get("fecha_inicio");
            String fecha = fechaInicio+" 00:00:00";
            Float tarifaNormal = Float.parseFloat(row.get("tarifa_normal"));
            Float tarifaExtra = Float.parseFloat(row.get("tarifa_extra"));
            Tarifa tarifa = new Tarifa(Timestamp.valueOf(fecha), tarifaNormal, tarifaExtra);
            tarifaService.guardarTarifa(tarifa);
        }
    }

    public void llenarTablaRol() throws IOException{
        Long id = 1L;
        Long id2 = 2L;
        Rol admin = new Rol(id, "Administrador");
        Rol encargadoMantenimiento = new Rol(id2, "Encargado de mantenimeiento");
        rolService.guardarRol(admin);
        rolService.guardarRol(encargadoMantenimiento);
    }
}
