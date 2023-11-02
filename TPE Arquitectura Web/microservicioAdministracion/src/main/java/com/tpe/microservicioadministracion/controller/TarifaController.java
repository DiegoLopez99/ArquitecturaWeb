package com.tpe.microservicioadministracion.controller;

import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import com.tpe.microservicioadministracion.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/tarifas")
public class TarifaController {
    @Autowired
    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> obtenerTarifas() {
        try {
            List<Tarifa> tarifas = tarifaService.getTarifas();
            if(tarifas.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron tarifas.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(tarifas);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron tarifas.");
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> obtenerTarifasPorId(@PathVariable Long id) {
        try {
            Tarifa tarifa = tarifaService.getTarifaId(id);
            return ResponseEntity.status(HttpStatus.OK).body(tarifa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la tarifa con ID: " + id);
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<Object> agregarTarifa(@RequestBody Tarifa tarifa) {
        try {
            Tarifa tarifaGuardada = tarifaService.guardarTarifa(tarifa);
            return ResponseEntity.status(HttpStatus.CREATED).body(tarifaGuardada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar la tarifa: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de la tarifa inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la tarifa: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> modificarTarifa(@PathVariable Long id, @RequestBody Tarifa tarifaNueva) {
        try {
            Tarifa tarifaActualizada = tarifaService.modificicarTarifa(id, tarifaNueva);
            return ResponseEntity.status(HttpStatus.OK).body(tarifaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de tarifa inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarTarifa(@PathVariable Long id) {
        try {
            boolean tarifaBorrada = tarifaService.eliminarTarifa(id);
            if (tarifaBorrada) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la tarifa con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de la tarifa inválido: " + e.getMessage());
        }
    }
}
