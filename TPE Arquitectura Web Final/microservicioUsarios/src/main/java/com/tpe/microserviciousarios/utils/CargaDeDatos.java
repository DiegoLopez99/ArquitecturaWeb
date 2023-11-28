package com.tpe.microserviciousarios.utils;

import com.tpe.microserviciousarios.domain.Cuenta;
import com.tpe.microserviciousarios.domain.Usuario;
import com.tpe.microserviciousarios.service.CuentaService;
import com.tpe.microserviciousarios.service.UsuarioService;
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
    private final CuentaService cuentaService;
    @Autowired
    private final UsuarioService usuarioService;

    public CargaDeDatos(CuentaService cuentaService, UsuarioService usuarioService) {
        this.cuentaService = cuentaService;
        this.usuarioService = usuarioService;
    }

    public void llenarTablaCuentas() throws IOException {
        CSVParser datosCuenta = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/cuentas.csv"));
        for (CSVRecord row : datosCuenta){
            Double saldo = Double.parseDouble(row.get("saldo"));
            Timestamp fechaAlta = Timestamp.valueOf(row.get("fecha_alta"));
            Cuenta cuenta = new Cuenta(saldo, fechaAlta, "activa");
            cuentaService.guardarCuenta(cuenta);
        }
    }

    public void llenarTablaUsuarios() throws IOException {
        CSVParser datosUsuarios = CSVFormat.DEFAULT.withHeader().parse(new FileReader("./src/main/resources/CSV/usuarios.csv"));
        for (CSVRecord row : datosUsuarios){
            String nombre = row.get("first_name");
            String apellido = row.get("last_name");
            String email = row.get("email");
            Long nroTelefono = Long.parseLong(row.get("nro_telefono"));
            String nombreUsuario = row.get("nombre_usuario");
            String contrasenia = row.get("contrasenia");
            Usuario usuario = new Usuario(nombre, apellido, nroTelefono, email, nombreUsuario, contrasenia);
            usuarioService.guardarUsuario(usuario);
        }
    }
}
