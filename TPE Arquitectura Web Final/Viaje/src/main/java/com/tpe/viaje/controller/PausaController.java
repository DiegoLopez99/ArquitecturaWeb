package com.tpe.viaje.controller;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.security.AuthorityConstants;
import com.tpe.viaje.service.PausaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/vp/pausas")
public class PausaController {

    private final PausaService pausaService;
    @Autowired
    public PausaController(PausaService pausaService) {
        this.pausaService = pausaService;
    }


    @GetMapping("/")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerPausas() {
        try {
            List<Pausa> pausas = pausaService.obtenerPausa();
            if(pausas.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron cuentas.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(pausas);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron cuentas.");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerPausaPorid(@PathVariable String id) {
        try {
            Pausa pausa = pausaService.obtenerPausaPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(pausa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la pausa con id: " + id);
        }
    }
    @GetMapping("/obtenerPausasMonopatin/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerPausasMonopatin(@PathVariable Long id) {
        try {
            List<Pausa> pausa = pausaService.obtenerPausasMonopatinId(id);
            return ResponseEntity.status(HttpStatus.OK).body(pausa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron pausas del monopatin con id: " + id);
        }
    }

    @GetMapping("/obtenerTiempoMonopatin")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerTiempoPausaMonopatin() {
        System.out.println("Iniciando obtenerTiempoPausaMonopatin...");
        try {
            List<List<Integer>> pausa = pausaService.obtenerTiempDePausaPorMonopatinId();
            System.out.println("Solicitud procesada. Resultado: " + pausa);
            return ResponseEntity.status(HttpStatus.OK).body(pausa);
        } catch (RuntimeException e) {
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudieron encontrar los datos requeridos" );
        }
    }

    @PostMapping("/guardar")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> guardarPausa(@RequestBody Pausa pausa) {
        try {
            Pausa pausaGuardada = pausaService.guardarPausa(pausa);
            return ResponseEntity.status(HttpStatus.CREATED).body(pausaGuardada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar la pausa: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de pausa inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la pausa: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> actualizarPausa(@PathVariable String id, @RequestBody Pausa pausa) {
        try {
            Pausa pausaActualizado = pausaService.actualizarPausa(id, pausa);
            return ResponseEntity.status(HttpStatus.OK).body(pausaActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de la pausa inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> borrarPausa(@PathVariable String id) {
        try {
            boolean pausaBorrada = pausaService.borrarPausa(id);
            if (pausaBorrada) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la pausa con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de la pausa inválido: " + e.getMessage());
        }
    }
}
