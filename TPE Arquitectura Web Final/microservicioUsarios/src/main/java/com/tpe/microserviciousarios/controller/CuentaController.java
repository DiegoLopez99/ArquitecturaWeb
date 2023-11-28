package com.tpe.microserviciousarios.controller;

import com.tpe.microserviciousarios.domain.Cuenta;
import com.tpe.microserviciousarios.security.AuthorityConstants;
import com.tpe.microserviciousarios.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uc/cuentas")
public class CuentaController {
    @Autowired
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerCuentas() {
        try {
            List<Cuenta> cuentas = cuentaService.getCuentas();
            if(cuentas.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron cuentas.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(cuentas);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron cuentas.");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerCuentaPorId(@PathVariable Long id) {
        try {
            Cuenta cuenta = cuentaService.getCuentaId(id);
            return ResponseEntity.status(HttpStatus.OK).body(cuenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la cuenta con ID: " + id);
        }
    }

    @PostMapping("/agregar")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> agregarCuenta(@RequestBody Cuenta cuenta) {
        try {
            Cuenta cuentaGuardada = cuentaService.guardarCuenta(cuenta);
            return ResponseEntity.status(HttpStatus.CREATED).body(cuentaGuardada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar la cuenta: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos la cuenta inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> modificarCuenta(@PathVariable Long id, @RequestBody Cuenta cuentaNueva) {
        try {
            Cuenta cuentaActualizada = cuentaService.modificicarCuenta(id, cuentaNueva);
            return ResponseEntity.status(HttpStatus.OK).body(cuentaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de cuenta inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> eliminarCuenta(@PathVariable Long id) {
        try {
            boolean cuentaBorrada = cuentaService.eliminarCuenta(id);
            if (cuentaBorrada) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la cuenta con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de la cuenta inválido: " + e.getMessage());
        }
    }
}
